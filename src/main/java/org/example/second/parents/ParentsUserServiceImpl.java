package org.example.second.parents;

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
import org.example.second.sms.SmsService;
import org.example.second.parents.model.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
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
    private final SmsService smsService;
    @Value("${coolsms.api.caller}") private String coolsmsApiCaller;

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
        if ( p.getAddr() != null && p.getZoneCode() != null ){
            p.setAddrs(p.getZoneCode(), p.getAddr()) ;
        }
        String password = passwordEncoder.encode(p.getUpw());
        p.setUpw(password);
        int result = mapper.postParentsUser(p);
        if(result != 1){
            throw new IllegalArgumentException("회원가입에 실패했습니다.");
        }
        return result;
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
    @Override // 문자발송 비밀번호 찾기
    public GetFindPasswordRes getFindPassword(GetFindPasswordReq req) {
        // 랜덤한 문자열 만들기
        String randomValue = SmsService.generateRandomMessage(8);
        List<ParentsUserEntity> list = mapper.getParentUserList(req);
        // 회원정보 확인
        if(list == null || list.isEmpty()){
            throw new IllegalArgumentException("회원정보가 존재하지 않습니다.");
        }
        GetParentsUserReq p = new GetParentsUserReq();
        p.setSignedUserId(list.get(0).getParentsId());
        ParentsUserEntity entity = mapper.getParentsUser(p);

        // 랜덤 문자열로 비밀번호 변경
        String password = passwordEncoder.encode(randomValue);
        PatchPasswordReq pp = new PatchPasswordReq();
        pp.setParentsId(entity.getParentsId());
        pp.setNewUpw(password);
        // 비밀번호 업데이트
        mapper.patchPassword(pp);
        // 변경정보 세팅
        GetFindPasswordRes res = new GetFindPasswordRes();
        res.setUpw(pp.getNewUpw());

        // 문자보내기
        smsService.sendPasswordSms(req.getPhone(), coolsmsApiCaller, randomValue);
        return res;
    }
    @Override @Transactional // 전자서명
    public SignatureRes signature(MultipartFile pic, SignatureReq req){
        if (pic == null || pic.isEmpty()) {
            throw new RuntimeException("서명 파일이 없습니다.");
        }
        try{
            String path = String.format("sign/%d", req.getSignId()) ;
            String fullPath = customFileUtils.makeFolders(path) ;
            String saveFileName = customFileUtils.makeRandomFileName(pic) ;
            String target1 = saveFileName ;
            String target = String.format("%s/%s", path, saveFileName) ;

            customFileUtils.transferTo(pic, target);
            req.setPic(target1) ;

            int result = mapper.signature(req) ;
        } catch (Exception e) {
            log.error("File upload error", e);
            throw new RuntimeException("서명 등록 오류가 발생했습니다: " + e.getMessage());
        }
        return SignatureRes
                .builder()
                .signId(req.getSignId())
                .pics(req.getPic())
                .build() ;
    }
}
