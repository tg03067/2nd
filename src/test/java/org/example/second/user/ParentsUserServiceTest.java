package org.example.second.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.maven.model.Parent;
import org.example.second.common.AppProperties;
import org.example.second.common.CookieUtils;
import org.example.second.common.CustomFileUtils;
import org.example.second.security.AuthenticationFacade;
import org.example.second.security.MyUser;
import org.example.second.security.MyUserDetails;
import org.example.second.security.jwt.JwtTokenProviderV2;
import org.example.second.sms.SmsService;
import org.example.second.user.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestPropertySource(
        properties = {
                "file.dir=D:\\download\\2nd"
        }
)
@ExtendWith(SpringExtension.class)
@Import({ParentsUserServiceImpl.class})
class ParentsUserServiceTest {
    @Value("${file.dir}") String uploadPath;
    @MockBean private ParentsUserMapper mapper;
    @Autowired private ParentsUserService service;
    @MockBean private CustomFileUtils customFileUtils;
    @MockBean private PasswordEncoder passwordEncoder;
    @MockBean private JwtTokenProviderV2 jwtTokenProvider;
    @MockBean private CookieUtils cookieUtils;
    @MockBean private AuthenticationFacade authenticationFacade;
    @MockBean private Authentication authentication ;
    @MockBean private AppProperties appProperties;
    @MockBean private SmsService smsService ;
    private final String ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$";
    private final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$";
    private final Pattern idPattern = Pattern.compile(ID_PATTERN);
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    @Value("${coolsms.api.caller}") private String coolsmsApiCaller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);

        MyUserDetails myUserDetails = mock(MyUserDetails.class);
        MyUser myUser = new MyUser();
        myUser.setUserId(1L);
        given(authentication.getPrincipal()).willReturn(myUserDetails);
        given(myUserDetails.getUser()).willReturn(myUser);
    }
    @Test @DisplayName("post 1") // 회원가입
    void postParentsUser() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("pG123456");
        p1.setUpw("aAbB!@1212");
        p1.setNm("홍길동");
        p1.setEmail("12345@naver.com");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");

        PostParentsUserReq p2 = new PostParentsUserReq();
        p2.setUid("pG234567");
        p2.setUpw("aAbB!@1212");
        p2.setNm("김길동");
        p1.setEmail("12345678@naver.com");
        p2.setPhone("010-2345-2345");
        p2.setConnet("모");

        given(mapper.postParentsUser(p1)).willReturn(1);
        given(mapper.postParentsUser(p2)).willReturn(1);

        assertEquals(1, service.postParentsUser(p1),"1. 이상");
        assertEquals(1, service.postParentsUser(p2),"2. 이상");

        verify(mapper).postParentsUser(p1);
        verify(mapper).postParentsUser(p2);
    }
    @Test @DisplayName("회원정보 확인") // 회원정보 확인
    void getParentsUser() {
        HttpServletResponse res = null ;
        long parentsId = 100 ;
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("pG123456");
        p1.setUpw("aAbB!@1212");
        p1.setNm("홍길동");
        p1.setEmail("12345@naver.com");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");
        p1.setParentsId(parentsId) ;

        String token = "accessToken" ;
        Authentication auth = mock(Authentication.class) ;
        MyUserDetails myUserDetails = mock(MyUserDetails.class) ;
        MyUser myUser = new MyUser() ;
        myUser.setUserId(p1.getParentsId());

        given(jwtTokenProvider.getAuthentication(token)).willReturn(auth) ;
        given(auth.getPrincipal()).willReturn(myUserDetails) ;
        given(myUserDetails.getUser()).willReturn(myUser) ;

        GetParentsUserReq req = new GetParentsUserReq() ;
        req.setSignedUserId(p1.getParentsId()) ;
        ParentsUserEntity entity = new ParentsUserEntity() ;
        entity.setParentsId(p1.getParentsId()) ;
        entity.setUid(p1.getUid());
        entity.setUpw(p1.getUpw());
        entity.setNm(p1.getNm());
        entity.setEmail(p1.getEmail());
        entity.setPhone(p1.getPhone());
        entity.setConnet(p1.getConnet());

        System.out.println("Request: " + req) ;
        System.out.println("Entity: " + entity);

        given(mapper.getParentsUser(eq(req))).willReturn(entity) ;

        ParentsUserEntity res0 = service.getParentsUser(token) ;

        System.out.println("ResultEntity: " + res0) ;

        assertEquals(entity, res0) ;
        verify(jwtTokenProvider).getAuthentication(token) ;
        verify(mapper).getParentsUser(eq(req)) ;

        long parentsId2 = 200L;
        String token2 = "accessToken2";

        PostParentsUserReq p2 = new PostParentsUserReq();
        p2.setUid("pG234567");
        p2.setUpw("aAbB!@1212");
        p2.setNm("김길동");
        p2.setEmail("12345678@naver.com");
        p2.setPhone("010-2345-2345");
        p2.setConnet("모");
        p2.setParentsId(parentsId2);

        Authentication auth2 = mock(Authentication.class);
        MyUserDetails myUserDetails2 = mock(MyUserDetails.class);
        MyUser myUser2 = new MyUser();
        myUser2.setUserId(parentsId2);

        given(jwtTokenProvider.getAuthentication(token2)).willReturn(auth2);
        given(auth2.getPrincipal()).willReturn(myUserDetails2);
        given(myUserDetails2.getUser()).willReturn(myUser2);

        GetParentsUserReq req2 = new GetParentsUserReq();
        req2.setSignedUserId(parentsId2);

        given(mapper.getParentsUser(eq(req2))).willReturn(null);

        ParentsUserEntity res2 = service.getParentsUser(token2);

        assertNull(res2);
        verify(jwtTokenProvider).getAuthentication(token2);
        verify(mapper).getParentsUser(eq(req2));

    }
    @Test @DisplayName("정보수정") // 회원정보 수정
    void patchParentsUser() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p1");
        p1.setNm("홍길동");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");
        p1.setParentsId(1L);
        ParentsUserEntity entity = new ParentsUserEntity();
        entity.setParentsId(p1.getParentsId());
        PatchParentsUserReq req = new PatchParentsUserReq();
        req.setParentsId(entity.getParentsId());
        req.setAddr("대구");
        given(mapper.patchParentsUser(req)).willReturn(1);
        assertNotEquals(p1.getAddr(), req.getAddr());
        int result = service.patchParentsUser(req);
        assertEquals(1, result);
        verify(mapper).patchParentsUser(req);

        ParentsUserEntity entity1 = new ParentsUserEntity();
        entity1.setParentsId(p1.getParentsId());
        PatchParentsUserReq req1 = new PatchParentsUserReq();
        req1.setParentsId(entity1.getParentsId());
        req1.setConnet("모");
        req1.setNm("유관순");
        given(mapper.patchParentsUser(req1)).willReturn(2);
        int result1 = service.patchParentsUser(req1);
        assertEquals(2, result1);

        ParentsUserEntity entity2 = new ParentsUserEntity();
        entity2.setParentsId(2L);
        PatchParentsUserReq req2 = new PatchParentsUserReq();
        req2.setParentsId(entity2.getParentsId());
        req2.setConnet("기타");
        given(mapper.patchParentsUser(req2)).willReturn(0);
        int result2 = service.patchParentsUser(req2);
        assertEquals(0, result2);
    }
    @Test @DisplayName("아이디 찾기") // 아이디 찾기
    void getFindId() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p1");
        p1.setNm("홍길동");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");
        p1.setParentsId(1L);

        ParentsUserEntity entity = new ParentsUserEntity();
        entity.setParentsId(p1.getParentsId());

        GetFindIdReq req = new GetFindIdReq();
        req.setPhone("010-1234-1234");
        req.setNm("홍길동");

        GetFindIdRes beforeRes = new GetFindIdRes();
        beforeRes.setUid(entity.getUid());

        given(mapper.getFindId(req)).willReturn(beforeRes);
        GetFindIdRes afterRes = service.getFindId(req);

        assertEquals(beforeRes.getUid(), afterRes.getUid());
        verify(mapper).getFindId(req);
    }
    @Test @DisplayName("아이디 찾기2") // 아이디 찾기
    void getFindId2() {
        GetFindIdReq req = new GetFindIdReq();
        req.setPhone("010-1234-3456");
        req.setNm("홍길동");

        given(mapper.getFindId(req)).willReturn(null);
        GetFindIdRes afterRes = service.getFindId(req);

        assertNull(afterRes);
        verify(mapper).getFindId(req);
    }
    @Test @DisplayName("비밀번호 수정") // 비밀번호 수정
    void patchPassword() {
        String encodedOldPassword = "encodedOldPassword";
        String encodedNewPassword = "encodedNewPassword";
        ParentsUserEntity p1 = new ParentsUserEntity();
        p1.setParentsId(1L);
        p1.setUid("pG123456");
        p1.setUpw(encodedOldPassword);
        p1.setNm("홍길동");
        p1.setEmail("12345@naver.com");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");
        p1.setAuth("ROLE_USER");
        p1.setAcept(2);

        PatchPasswordReq req1 = new PatchPasswordReq();
        req1.setParentsId(p1.getParentsId());
        req1.setUid(p1.getUid());
        req1.setUpw("aAbB!@1212");
        req1.setNewUpw(encodedNewPassword);

        GetParentsUserReq q = new GetParentsUserReq();
        q.setSignedUserId(p1.getParentsId());;
        given(mapper.getParentsUser(q)).willReturn(p1);
        given(passwordEncoder.matches(req1.getUpw(), encodedOldPassword)).willReturn(true);
        given(passwordEncoder.encode(req1.getNewUpw())).willReturn(encodedNewPassword);
        given(mapper.patchPassword(any(PatchPasswordReq.class))).willReturn(1);

        int result = assertDoesNotThrow(() -> service.patchPassword(req1));

        assertEquals(1, result);
        verify(mapper).patchPassword(argThat(req ->
                req.getParentsId() == p1.getParentsId() &&
                req.getNewUpw().equals(encodedNewPassword)
        ));
    }
    @Test @DisplayName("로그인") // 로그인
    void signInPost() {
        HttpServletResponse res = null;
        SignInPostReq req1 = new SignInPostReq();
        req1.setUid("p1");
        req1.setUpw("1212");
        String password = passwordEncoder.encode(req1.getUpw());

        ParentsUser user1 = new ParentsUser();
        user1.setParentsId(1L);
        user1.setUid(req1.getUid());
        user1.setUpw(password);
        user1.setNm("길동");
        user1.setPhone("010-1234-1234");
        user1.setConnet("부");
        user1.setAuth("ROLE_USER");
        user1.setAcept(2);
        user1.setCreatedAt("2024-07-01 15:49:23");

        given(mapper.signInPost(req1.getUid())).willReturn(user1);

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        given(jwtTokenProvider.generateAccessToken(any(MyUser.class))).willReturn(accessToken);
        given(jwtTokenProvider.generateRefreshToken(any(MyUser.class))).willReturn(refreshToken);

        AppProperties.Jwt jwtProperties = mock(AppProperties.Jwt.class);
        given(appProperties.getJwt()).willReturn(jwtProperties);
        given(jwtProperties.getRefreshTokenCookieMaxAge()).willReturn(3600);

        try (MockedStatic<BCrypt> mockedStatic = mockStatic(BCrypt.class)) {
            mockedStatic.when(() -> BCrypt.checkpw(req1.getUpw(), password)).thenReturn(true);

            SignInPostRes res1 = service.signInPost(req1, res);

            assertEquals(user1.getParentsId(), res1.getParentsId(), "1. 이상");
            assertEquals(user1.getNm(), res1.getNm(),"2. 이상");
            assertEquals(accessToken, res1.getAccessToken(), "3. 이상");

            mockedStatic.verify(() -> BCrypt.checkpw(req1.getUpw(), password));
        }
        verify(mapper).signInPost(req1.getUid());
        verify(jwtTokenProvider).generateAccessToken(any(MyUser.class));
        verify(jwtTokenProvider).generateRefreshToken(any(MyUser.class));
        verify(cookieUtils).deleteCookie(res, "refresh-token");
        verify(cookieUtils).setCookie(res, "refresh-token",refreshToken,3600);
    }
    @Test @DisplayName("accessToken 가져오기 - 성공")
    void getAccessToken() {
        HttpServletRequest req = mock(HttpServletRequest.class);

        Cookie cookie = new Cookie("refresh-token", "valid-refresh-token");
        given(cookieUtils.getCookie(req, "refresh-token")).willReturn(cookie);

        given(jwtTokenProvider.isValidateToken("valid-refresh-token")).willReturn(true);

        MyUser myUser = MyUser.builder().
                userId(1).
                role("ROLE_PARENTS").
                build();
        UserDetails userDetails = new MyUserDetails(myUser);
        given(jwtTokenProvider.getUserDetailsFromToken("valid-refresh-token")).willReturn(userDetails);
        given(jwtTokenProvider.generateAccessToken(myUser)).willReturn("new-access-token");

        Map result = service.getAccessToken(req);
        assertEquals("new-access-token", result.get("accessToken"));
        verify(cookieUtils).getCookie(req, "refresh-token");
        verify(jwtTokenProvider).isValidateToken("valid-refresh-token");
        verify(jwtTokenProvider).getUserDetailsFromToken("valid-refresh-token");
        verify(jwtTokenProvider).generateAccessToken(myUser);
    }
    @Test @DisplayName("AccessToken 가져오기 - 실패 케이스: 쿠키 없음")
    void getAccessTokenNoCookie() {
        HttpServletRequest req = mock(HttpServletRequest.class);

        given(cookieUtils.getCookie(req, "refresh-token")).willReturn(null);

        assertThrows(RuntimeException.class, () -> service.getAccessToken(req));
        verify(cookieUtils).getCookie(req, "refresh-token");
    }
    @Test @DisplayName("AccessToken 가져오기 - 실패 케이스: 토큰 검증 실패")
    void getAccessTokenInvalidToken() {
        HttpServletRequest req = mock(HttpServletRequest.class);

        Cookie cookie = new Cookie("refresh-token", "invalid-refresh-token");
        given(cookieUtils.getCookie(req, "refresh-token")).willReturn(cookie);

        given(jwtTokenProvider.isValidateToken("invalid-refresh-token")).willReturn(false);

        assertThrows(RuntimeException.class, () -> service.getAccessToken(req));
        verify(cookieUtils).getCookie(req, "refresh-token");
        verify(jwtTokenProvider).isValidateToken("invalid-refresh-token");
    }
}



























