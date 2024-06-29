package org.example.second.user;

import org.example.second.common.AppProperties;
import org.example.second.common.CookieUtils;
import org.example.second.common.CustomFileUtils;
import org.example.second.security.AuthenticationFacade;
import org.example.second.security.jwt.JwtTokenProviderV2;
import org.example.second.user.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
@TestPropertySource(
        properties = {
                "E:\2024-01\2nd"
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
    @MockBean private AppProperties appProperties;

    @Test @DisplayName("post 1")
    void postParentsUser() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("pG123456");
        p1.setUpw("aAbB!@1212");
        p1.setNm("홍길동");
        p1.setEmail("12345@naver.com");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");
        p1.setAuth("ROLE_USER");
        p1.setAcept(2);
        PostParentsUserReq p2 = new PostParentsUserReq();
        p2.setUid("pG234567");
        p2.setUpw("aAbB!@1212");
        p2.setNm("김길동");
        p1.setEmail("12345678@naver.com");
        p2.setPhone("010-2345-2345");
        p2.setConnet("모");
        p2.setAuth("ROLE_USER");
        p2.setAcept(2);
        given(mapper.postParentsUser(p1)).willReturn(0);
        given(mapper.postParentsUser(p2)).willReturn(1);
        assertEquals(0, service.postParentsUser(p1),"1. 이상");
        assertEquals(1, service.postParentsUser(p2),"2. 이상");
        verify(mapper).postParentsUser(p1);
        verify(mapper).postParentsUser(p2);
    }
    @Test @DisplayName("회원정보 확인")
    void getParentsUser() {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p1");
        p1.setNm("홍길동");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");
        p1.setAuth("ROLE_USER");
        p1.setAcept(2);
        p1.setParentsId(1L);
        GetParentsUserReq req1 = new GetParentsUserReq();
        req1.setSignedUserId(p1.getParentsId());
        ParentsUserEntity result1 = new ParentsUserEntity();
        result1.setParentsId(p1.getParentsId());
        given(mapper.getParentsUser(req1)).willReturn(result1);
        ParentsUserEntity res1 = service.getParentsUser(req1);
        assertEquals(result1, res1);
        verify(mapper).getParentsUser(req1);


        PostParentsUserReq p2 = new PostParentsUserReq();
        p2.setUid("p2");
        p2.setNm("김길동");
        p2.setPhone("010-2345-2345");
        p2.setConnet("모");
        p2.setAuth("ROLE_USER");
        p2.setAcept(2);
        p2.setParentsId(2L);
        GetParentsUserReq req2 = new GetParentsUserReq();
        req2.setSignedUserId(p2.getParentsId());
        ParentsUserEntity result2 = new ParentsUserEntity();
        result2.setParentsId(p2.getParentsId());
        given(mapper.getParentsUser(req2)).willReturn(result2);
        ParentsUserEntity res2 = service.getParentsUser(req2);
        assertEquals(result2, res2);
        verify(mapper).getParentsUser(req2);
    }

    @Test @DisplayName("정보수정")
    void patchParentsUser() {

    }

    @Test @DisplayName("아이디 찾기")
    void getFindId() {

    }

    @Test @DisplayName("비밀번호 수정")
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
}