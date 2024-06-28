package org.example.second.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.second.common.AppProperties;
import org.example.second.security.MyUser;
import org.example.second.security.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProviderV2 {
    private final ObjectMapper om;
    private final AppProperties appProperties;
    private final SecretKey secretKey;

    public JwtTokenProviderV2(ObjectMapper om, AppProperties appProperties) {
        this.om = om;
        this.appProperties = appProperties;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(appProperties.getJwt().getSecret()));
    }
    public String generateAccessToken(MyUser myUser) {
        return generateToken(myUser, appProperties.getJwt().getAccessTokenExpiry());
        // yaml 파일에서 app.jw.access-token-expiry 내용을 가져오는 부분
    }
    public String generateRefreshToken(MyUser myUser) {
        return generateToken(myUser, appProperties.getJwt().getRefreshTokenExpiry());
        // yaml 파일에서 app.jw.refresh-token-expiry 내용을 가져오는 부분
    }
    private String generateToken(MyUser myUser, long tokenValidMilliSecond) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // JWT 생성일시
                .expiration(new Date(System.currentTimeMillis() + tokenValidMilliSecond)) // JWT 만료일시
                .claims(createClaims(myUser)) // claims 는 payload 에 저장하고 싶은 내용을 저장
                .signWith(secretKey, Jwts.SIG.HS512)  // 서명 ( JWT 암호화 선택, 위변조 검증 )
                .compact();
                // 토큰 생성

        }
    private Claims createClaims(MyUser myUser) {
        // JWT body 에 담는 내용
        try {
            String json = om.writeValueAsString(myUser); // 객채 to JSON
            return Jwts.claims().add("signedUser", json).build(); // Claims 에 JSON 저장
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public Claims getAllClaims(String token) {
        // token : 암호화 된 부분을 받아서 Payload 부분을 뺴내겠다/
        return Jwts
                .parser()
                .verifyWith(secretKey) // 똑같은 키로 복호화
                .build()
                .parseSignedClaims(token)
                .getPayload(); // JWT 안에 들어있는 payload ( Claims ) 를 리턴
    }
    public UserDetails getUserDetailsFromToken(String token) {
        try {
            Claims claims = getAllClaims(token); // JWT(인증코드) 에 저장되어 있는 Claims 를 얻어온다.
            String json = (String)claims.get("signedUser"); // Claims 에 저장되어 있는 값을 얻어온다. ( 그것이 JSON (Data))
            MyUser user = om.readValue(json, MyUser.class); // JSON > 객체로 변환 (그것이 UserDetails , 정확히는 MyUserDetails )
            MyUserDetails userDetails = new MyUserDetails();
            userDetails.setUser(user);
            return userDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = getUserDetailsFromToken(token);
        return userDetails == null ? null
                // JWT Token 에 저장공간이 하나 할당되기를 원함.
                // userDetails = 로그인한 사용자 pk값을 빼내기 위해서
                : new UsernamePasswordAuthenticationToken(userDetails ,
                null ,userDetails.getAuthorities()
                // 권한(인가) 확인 부분
        );
    }
    public boolean isValidateToken(String token) {
        try {
            return !getAllClaims(token).getExpiration().before(new Date());
           } catch (Exception e){
            return false;
        }
    }
    public String resolveToken(HttpServletRequest req) {
        String jwt = req.getHeader(appProperties.getJwt().getHeaderSchemaName());
        log.debug("JWT from Header: {}", jwt);
        if(jwt == null){ log.debug("JWT is Null"); return null; }

        if(!jwt.startsWith(appProperties.getJwt().getTokenType())) {
            log.debug("JWT does not start with {}", appProperties.getJwt().getTokenType());
            return null;
        }

        String token = jwt.substring(appProperties.getJwt().getTokenType().length()).trim();
        log.debug("Resolved JWT: {}", token);
        return token;
    }
}