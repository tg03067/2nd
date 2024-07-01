package org.example.second.user;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.annotations.Mapper;
import org.example.second.user.model.*;

import java.util.List;

@Mapper
public interface ParentsUserMapper {
    // 회원가입
    int postParentsUser(PostParentsUserReq p);
    // 회원정보 확인
    ParentsUserEntity getParentsUser(GetParentsUserReq parentsId);
    // 회원 정보 수정
    int patchParentsUser(PatchParentsUserReq p);
    // 아이디 찾기
    GetFindIdRes getFindId(GetFindIdReq req);
    // 비밀번호 수정
    int patchPassword(PatchPasswordReq req);
    // 로그인
    ParentsUser signInPost(String uid);

    // TDD select
    List<ParentsUser> selTest(long parentsId);
}
