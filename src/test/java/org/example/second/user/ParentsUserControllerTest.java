package org.example.second.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.second.CharEncodingConfiguration;
import org.example.second.security.SecurityConfiguration;
import org.example.second.security.jwt.JwtTokenProviderV2;
import org.example.second.user.model.PostParentsUserReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({CharEncodingConfiguration.class, SecurityConfiguration.class})
@WebMvcTest(ParentsUserControllerImpl.class)
class ParentsUserControllerTest {
    @Autowired private ObjectMapper om;
    @Autowired private MockMvc mockMvc;
    @MockBean private ParentsUserService service;
    @MockBean private JwtTokenProviderV2 jwtTokenProviderV2;
    @Autowired private WebApplicationContext webApplicationContext;
    private final String BASE_URL = "/api/user/parents";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }

    @Test @DisplayName("회원가입 TDD")
    void postParents() throws Exception {
        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("pG123456");
        p1.setUpw("aAbB!@1212");
        p1.setNm("홍길동");
        p1.setEmail("12345@naver.com");
        p1.setPhone("010-1234-1234");
        p1.setConnet("부");
        p1.setParentsId(2L);

//        int result = 1;
//        given(service.postParentsUser(p1)).willReturn(result);
//
//        String json = om.writeValueAsString(p1);
//
//        ResponseEntity<Integer> expectedResponse = new ResponseEntity<>(result, HttpStatus.OK);
//        String expectedJson = om.writeValueAsString(expectedResponse);
//
//        MyUser myUser = new MyUser();
//        myUser.setUserId(p1.getParentsId());
//        myUser.setRole(p1.getAuth());
//
//        String generatedToken = "generated-jwt-token";
//        given(jwtTokenProviderV2.generateAccessToken(myUser)).willReturn(generatedToken);
//
//        String token = "Bearer " + generatedToken;
//
//        given(jwtTokenProviderV2.isValidateToken(generatedToken)).willReturn(true);
//        given(jwtTokenProviderV2.getAuthentication(generatedToken)).willReturn(new UsernamePasswordAuthenticationToken("user", null,
//                List.of(new SimpleGrantedAuthority("ROLE_PARENTS"))));
//
//        mockMvc.perform(post(BASE_URL + "/sign-up")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", token)
//                        .content(json)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(content().json(expectedJson))
//                .andDo(print());
//
//        verify(service).postParentsUser(p1);
        int result = 1;
        given(service.postParentsUser(any(PostParentsUserReq.class))).willReturn(result);

        String json = om.writeValueAsString(p1);

        mockMvc.perform(post(BASE_URL + "/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(result)))
                .andDo(print());

        verify(service).postParentsUser(any(PostParentsUserReq.class));
    }

    @Test
    void getParentsUser() {
    }

    @Test
    void patchParentsUser() {
    }

    @Test
    void getFindId() {
    }

    @Test
    void patchPassword() {
    }

    @Test
    void signInPost() {
    }

    @Test
    void getAccessToken() {
    }
}