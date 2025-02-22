package org.example.second.parents;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.second.parents.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ParentsUserService {
    // 회원가입
    int postParentsUser(PostParentsUserReq p);
    // 회원정보 조회
    ParentsUserEntity getParentsUser(String id);
    // 회원정보 수정
    int patchParentsUser(PatchParentsUserReq p);
    // 아이디 찾기
    GetFindIdRes getFindId(GetFindIdReq req);
    // 비밀번호 수정
    int patchPassword(PatchPasswordReq req);
    // 로그인
    SignInPostRes signInPost(SignInPostReq p, HttpServletResponse res);
    // 토큰정보 확인
    Map getAccessToken(HttpServletRequest req);
    // 비밀번호 찾기
    GetFindPasswordRes getFindPassword(GetFindPasswordReq req);
    // 전자서명 관련
    SignatureRes signature(MultipartFile pic, SignatureReq req) ;
}
