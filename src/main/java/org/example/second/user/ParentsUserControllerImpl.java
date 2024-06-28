package org.example.second.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user/parents")
@Slf4j
@Tag(name = "학부모 관련", description = "학부모 CRUD")
public class ParentsUserControllerImpl implements ParentsUserController {
    private final ParentsUserService service;

    // 학부모 회원가입

    // 학부모 로그인

    // 학부모 아이디 찾기

    // 학부모 비밀번호 찾기

    // 정보 수정

    // 과목별 성적 학인 ( 과목 별 원점수, 전체평균, 반평균, 전체등수, 반등수 )

    // 전자서명

    // 성적통계
}
