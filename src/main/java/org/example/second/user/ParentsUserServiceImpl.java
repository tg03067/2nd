package org.example.second.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.second.common.AppProperties;
import org.example.second.common.CookieUtils;
import org.example.second.common.CustomFileUtils;
import org.example.second.security.AuthenticationFacade;
import org.example.second.security.MyUser;
import org.example.second.security.MyUserDetails;
import org.example.second.security.jwt.JwtTokenProviderV2;
import org.example.second.user.model.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParentsUserServiceImpl implements ParentsUserService {
    private final ParentsUserMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AppProperties appProperties;
    private final String ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$";
    private final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$";
    private final Pattern idPattern = Pattern.compile(ID_PATTERN);
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private final String NAME_PATTERN  = "^[가-힣a-zA-Z\\s-]+$";

    @Override @Transactional // 회원가입
    public int postParentsUser(PostParentsUserReq p) {
        if(p.getUid() == null || p.getUpw() == null){
            throw new IllegalArgumentException("아이디와 비밀번호는 필수 입력사항입니다.");
        } else if (!idPattern.matcher(p.getUid()).matches()) {
            throw new IllegalArgumentException("아이디 형식이 잘못되었습니다.");
        } else if (!passwordPattern.matcher(p.getUpw()).matches()) {
            throw new IllegalArgumentException("비밀번호 형식이 잘못되었습니다.");
        } else if (p.getEmail() != null && !p.getEmail().isEmpty()) {
            if(!emailPattern.matcher(p.getEmail()).matches()) {
                throw new IllegalArgumentException("이메일 형식이 잘못되었습니다.");
            } else {
                p.setEmail(null);
            }
        }
        String password = passwordEncoder.encode(p.getUpw());
        p.setUpw(password);
        return mapper.postParentsUser(p);
    }
    @Override // 회원정보 조회
    public ParentsUserEntity getParentsUser(String token) {
        Authentication auth = jwtTokenProvider.getAuthentication(token) ;
        SecurityContextHolder.getContext().setAuthentication(auth) ;
        MyUserDetails userDetails = (MyUserDetails) auth.getPrincipal();
        long parentsId = userDetails.getUser().getUserId() ;
        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(parentsId);
        return mapper.getParentsUser(req);
    }
    @Override @Transactional // 회원정보 수정
    public int patchParentsUser(PatchParentsUserReq p) {
        p.setParentsId(authenticationFacade.getLoginUserId());
        return mapper.patchParentsUser(p);
    }
    @Override // 아이디 찾기
    public GetFindIdRes getFindId(GetFindIdReq req) {
        GetFindIdRes result = mapper.getFindId(req);
        if (result == null) {
            throw new RuntimeException("해당 요청에 대한 정보가 존재하지 않습니다.");
        }
        return result;
    }
    @Override @Transactional // 비밀번호 수정
    public int patchPassword(PatchPasswordReq p) {
        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(authenticationFacade.getLoginUserId());
        ParentsUserEntity entity = mapper.getParentsUser(req);
        if(Objects.isNull(entity)){
            throw new IllegalArgumentException("아이디를 확인해 주세요");
        } else if(!passwordEncoder.matches(p.getUpw(), entity.getUpw())) {
            log.info("p pass: {}, entity pass: {}", p.getUpw(), entity.getUpw());
            throw new IllegalArgumentException("비밀번호를 확인해 주세요");
        }
        String password = passwordEncoder.encode(p.getNewUpw());
        p.setParentsId(entity.getParentsId());
        p.setNewUpw(password);
        return mapper.patchPassword(p);
    }
    @Override // 로그인
    public SignInPostRes signInPost(SignInPostReq p, HttpServletResponse res) {
        ParentsUser user = mapper.signInPost(p.getUid());
        if(Objects.isNull(user)){
            throw new IllegalArgumentException("아이디를 확인해 주세요");
        } else if (!BCrypt.checkpw(p.getUpw(), user.getUpw())) {
            throw new IllegalArgumentException("비밀번호를 확인해 주세요");
        }

        MyUser myUser = MyUser.builder().
                userId(user.getParentsId()).
                role("ROLE_PARENTS").
                build();

        String accessToken = jwtTokenProvider.generateAccessToken(myUser);
        String refreshToken = jwtTokenProvider.generateRefreshToken(myUser);

        int refreshTokenMaxAge = appProperties.getJwt().getRefreshTokenCookieMaxAge();
        cookieUtils.deleteCookie(res, "refresh-token");
        cookieUtils.setCookie(res,"refresh-token", refreshToken, refreshTokenMaxAge);

        return SignInPostRes.builder().
                parentsId(user.getParentsId()).
                nm(user.getNm()).
                accessToken(accessToken).
                build();
    }
    @Override // access token
    public Map<String, Object> getAccessToken(HttpServletRequest req){
        Cookie cookie = cookieUtils.getCookie(req, "refresh-token");
        if(cookie == null){
            throw new RuntimeException();
        }
        String refreshToken = cookie.getValue();
        if(!jwtTokenProvider.isValidateToken(refreshToken)){
            throw new RuntimeException();
        }
        UserDetails auth = jwtTokenProvider.getUserDetailsFromToken(refreshToken);
        MyUser myUser = ((MyUserDetails)auth).getUser();
        String accessToken = jwtTokenProvider.generateAccessToken(myUser);

        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        return map;
    }
    @Override
    public GetFindPasswordRes getFindPassword(GetFindPasswordReq req) {
        return null;
    }
}
