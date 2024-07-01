package org.example.second.user;

import org.example.second.user.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
@MybatisTest
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ParentsUserMapperTest {
    @Autowired ParentsUserMapper mapper;

    @Test @DisplayName("학부모 post")
    void postParentsUser() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnet("부");
        p.setAuth("ROLE_USER");
        p.setAcept(2);
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);
        GetParentsUserReq req1 = new GetParentsUserReq();
        req1.setSignedUserId(p.getParentsId());
        ParentsUserEntity res = mapper.getParentsUser(req1);
        assertNotNull(res);
        assertEquals(p.getParentsId(), res.getParentsId(), "1. 이상");

        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p2");
        p1.setUpw("1212");
        p1.setNm("김길동");
        p1.setPhone("010-5678-5678");
        p1.setConnet("모");
        p1.setAuth("ROLE_USER");
        p1.setAcept(2);
        int affectedRow1 = mapper.postParentsUser(p1);
        assertEquals(1, affectedRow1);
        GetParentsUserReq req2 = new GetParentsUserReq();
        req2.setSignedUserId(p.getParentsId());
        ParentsUserEntity res1 = mapper.getParentsUser(req2);
        assertNotNull(res1);
        assertEquals(p1.getParentsId(), res1.getParentsId(), "2. 이상");
    }
    @Test @DisplayName("post 1")
    void postParentsUser2() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("김철수");
        p.setPhone("010-1234-1234");
        p.setSubPhone("010-1111-1111");
        p.setEmail("test@example.com");
        p.setConnet("Y");
        p.setAuth("ROLE_USER");
        p.setAddr("Seoul");
        p.setAcept(0);
        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(p.getParentsId());
        // 학부모 정보 저장
        int affectedRow = mapper.postParentsUser(p);
        System.out.println("Affected Rows (first insert): " + affectedRow);
        assertEquals(1, affectedRow, "1. 예상된 행 수");

        // 저장된 학부모 정보 가져오기
        System.out.println("Generated ParentsId (first insert): " + p.getParentsId());
        ParentsUserEntity res = mapper.getParentsUser(req);
        System.out.println("GetParentsUserRes (first select): " + res);
        assertNotNull(res, "1. 예상된 학부모 정보는 null이 아닙니다.");
        assertEquals(p.getParentsId(), res.getParentsId(), "1. 예상된 학부모 ID");

        PostParentsUserReq p1 = new PostParentsUserReq();
        p1.setUid("p2");
        p1.setUpw("5678");
        p1.setNm("김영희");
        p1.setPhone("010-5678-5678");
        p1.setSubPhone("010-2222-2222");
        p1.setEmail("test2@example.com");
        p1.setConnet("Y");
        p1.setAuth("ROLE_USER");
        p1.setAddr("Busan");
        p1.setAcept(0);
        GetParentsUserReq req1 = new GetParentsUserReq();
        req1.setSignedUserId(p1.getParentsId());
        // 두 번째 학부모 정보 저장
        int affectedRow1 = mapper.postParentsUser(p1);
        System.out.println("Affected Rows (second insert): " + affectedRow1);
        assertEquals(1, affectedRow1, "2. 예상된 행 수");

        // 두 번째로 저장된 학부모 정보 가져오기
        System.out.println("Generated ParentsId (second insert): " + p1.getParentsId());
        ParentsUserEntity res1 = mapper.getParentsUser(req1);
        System.out.println("GetParentsUserRes (second select): " + res1);
        assertNotNull(res1, "2. 예상된 학부모 정보는 null이 아닙니다.");
        assertEquals(p1.getParentsId(), res1.getParentsId(), "2. 예상된 학부모 ID");
    }
    @Test @DisplayName("patch 1")
    void patchParentsUser() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnet("부");
        p.setAuth("ROLE_USER");
        p.setAcept(2);
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);

        PatchParentsUserReq req1 = new PatchParentsUserReq();
        req1.setParentsId(p.getParentsId());
        req1.setAddr("대구");
        req1.setSubPhone("010-1111-1111");
        req1.setEmail("test@example.com");
        int affectedRow1 = mapper.patchParentsUser(req1);
        assertEquals(0, affectedRow1, "1. 이상");
    }
    @Test @DisplayName("아이디 찾기")
    void getFindId() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnet("부");
        p.setAuth("ROLE_USER");
        p.setAcept(2);
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);

        GetFindIdReq req = new GetFindIdReq();
        req.setNm("홍길동");
        req.setPhone("010-1234-1234");
        GetFindIdRes res = mapper.getFindId(req);
        assertEquals(p.getUid(), res.getUid());
    }
    @Test @DisplayName("비밀번호 변경")
    void patchPassword() {
        PostParentsUserReq p = new PostParentsUserReq();
        p.setUid("p1");
        p.setUpw("1212");
        p.setNm("홍길동");
        p.setPhone("010-1234-1234");
        p.setConnet("부");
        p.setAuth("ROLE_USER");
        p.setAcept(2);
        GetParentsUserReq req = new GetParentsUserReq();
        req.setSignedUserId(p.getParentsId());
        int affectedRow = mapper.postParentsUser(p);
        assertEquals(1, affectedRow);

        PatchPasswordReq req1 = new PatchPasswordReq();
        req1.setUid(p.getUid());
        req1.setParentsId(p.getParentsId());
        req1.setUpw("1212");
        req1.setNewUpw("123123");
        int affectedRow1 = mapper.patchPassword(req1);
        assertEquals(1, affectedRow1);
        mapper.getParentsUser(req);
    }
    @Test @DisplayName("로그인")
    void signInPost() {
        PostParentsUserReq user = new PostParentsUserReq();
        user.setUid("p1");
        user.setUpw("1212");
        user.setNm("홍길동");
        user.setPhone("010-1234-1234");
        user.setConnet("부");
        user.setAuth("ROLE_USER");
        user.setAcept(2);
        mapper.postParentsUser(user);

        ParentsUser user1 = mapper.signInPost(user.getUid());
        if(user1 != null){
            GetParentsUserReq req1 = new GetParentsUserReq();
            req1.setSignedUserId(user1.getParentsId());
            List<ParentsUser> userList = mapper.selTest(req1.getSignedUserId());
            ParentsUser user1comp = userList.get(0);
            assertEquals(user1comp, user1);

            ParentsUser userNo = mapper.signInPost("asdf");
            assertNull(userNo);
        }
    }
}