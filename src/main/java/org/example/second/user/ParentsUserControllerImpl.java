package org.example.second.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.second.user.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/parents")
@Slf4j
@Tag(name = "학부모 관련", description = "학부모 CRUD")
public class ParentsUserControllerImpl implements ParentsUserController {
    private final ParentsUserService service;
    // 학부모 회원가입
    @Override @PostMapping("sign-up") @Operation(summary = "회원가입")
    public ResponseEntity<Integer> postParents(PostParentsUserReq p) {
        int result = service.postParentsUser(p);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    // 정보 조회
    @Override
    public ResponseEntity<ParentsUserEntity> getParentsUser(GetParentsUserReq p) {
        return null;
    }
    // 정보 수정
    @Override
    public ResponseEntity<Integer> patchParentsUser(PatchParentsUserReq p) {
        return null;
    }
    // 학부모 아이디 찾기
    @Override
    public ResponseEntity<GetFindIdRes> getFindId(GetFindIdReq p) {
        return null;
    }
    // 비밀번호 수정
    @Override
    public ResponseEntity<Integer> patchPassword(PatchPasswordReq p) {
        return null;
    }
    // 학부모 로그인
    @Override
    public ResponseEntity<SignInPostRes> signInPost(SignInPostReq p, HttpServletResponse res) {
        return null;
    }
    // 토큰확인
    @Override
    public ResponseEntity<Map> getAccessToken(HttpServletRequest req) {
        return null;
    }
    // 학부모 비밀번호 찾기










    // 과목별 성적 학인 ( 과목 별 원점수, 전체평균, 반평균, 전체등수, 반등수 )

    // 전자서명

    // 성적통계
}
