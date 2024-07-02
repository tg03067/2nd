package org.example.second.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.second.security.MyUser;
import org.example.second.security.MyUserDetails;
import org.example.second.security.jwt.JwtTokenProviderV2;
import org.example.second.user.model.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/parents")
@Slf4j
@Tag(name = "학부모 관련", description = "학부모 CRUD")
public class ParentsUserControllerImpl implements ParentsUserController {
    private final ParentsUserServiceImpl service;
    private final JwtTokenProviderV2 tokenProvider;
    private final ObjectMapper om;
    // 학부모 회원가입
    @Override @PostMapping("/sign-up") @Operation(summary = "회원가입")
    public ResponseEntity<Integer> postParents(@RequestBody PostParentsUserReq p) {
        int result = service.postParentsUser(p) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 정보 조회
    @Override @GetMapping("/parent-info") @Operation(summary = "정보조회")
    public ResponseEntity<ParentsUserEntity> getParentsUser(HttpServletRequest req) {
        String token = tokenProvider.resolveToken(req) ;
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() ;
        }
        ParentsUserEntity p = service.getParentsUser(token);
        return ResponseEntity.ok().body(p) ;
    }
    // 정보 수정
    @Override @PutMapping("/info-update") @Operation(summary = "정보수정")
    public ResponseEntity<Integer> patchParentsUser(@RequestBody PatchParentsUserReq p) {
        int result = service.patchParentsUser(p) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 학부모 아이디 찾기
    @Override @GetMapping("/find-id") @Operation(summary = "아이디 찾기")
    public ResponseEntity<GetFindIdRes> getFindId(@ModelAttribute @ParameterObject GetFindIdReq p) {
        GetFindIdRes res = service.getFindId(p) ;
        return ResponseEntity.ok().body(res) ;
    }
    // 비밀번호 수정
    @Override @PutMapping("/password-update") @Operation(summary = "비밀번호 수정")
    public ResponseEntity<Integer> patchPassword(@RequestBody PatchPasswordReq p) {
        int result = service.patchPassword(p) ;
        return ResponseEntity.ok().body(result) ;
    }
    // 학부모 로그인
    @Override @PostMapping("/sign-in") @Operation(summary = "로그인")
    public ResponseEntity<SignInPostRes> signInPost(@RequestBody SignInPostReq p, HttpServletResponse res) {
        SignInPostRes postRes = service.signInPost(p, res) ;
        return ResponseEntity.ok().body(postRes) ;
    }
    // 토큰확인
    @Override @GetMapping("/access-token") @Operation(summary = "accessToken - 확인")
    public ResponseEntity<Map> getAccessToken(@ModelAttribute @ParameterObject HttpServletRequest req) {
        Map<String, String> res = service.getAccessToken(req) ;
        return ResponseEntity.ok().body(res) ;
    }

    // 학부모 비밀번호 찾기

    // 과목별 성적 학인 ( 과목 별 원점수, 전체평균, 반평균, 전체등수, 반등수 )

    // 전자서명

    // 성적통계
}
