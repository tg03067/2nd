package org.example.second.subject;

import org.example.second.scoremaster.subject.SubjectMapper;
import org.example.second.scoremaster.subject.model.DeleteSubjectReq;
import org.example.second.scoremaster.subject.model.GetSubjectReq;
import org.example.second.scoremaster.subject.model.GetSubjectRes;
import org.example.second.scoremaster.subject.model.PostSubjectReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("tdd")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SubjectMapperTest {

    @Autowired private SubjectMapper mapper;

    @Test
    @DisplayName("과목 post 테스트")
    void postSubject() {
        PostSubjectReq req = new PostSubjectReq();
        req.setSubjectId(1);
        req.setSubjectName("english");
        req.setGrade(100);
        int affectedRow = mapper.postSubject(req);
        assertEquals(1, affectedRow, "1. 먼가이상");

        PostSubjectReq req2 = new PostSubjectReq();
        req2.setSubjectId(2);
        req2.setSubjectName("korean");
        req2.setGrade(96);
    }

    @Test
    @DisplayName("과목 get 테스트")
    void getSubject() {
        GetSubjectReq req = new GetSubjectReq();
        req.setSubjectId(1);
        List<GetSubjectRes> list = mapper.getSubjectList(req);
        assertEquals(0, list.size(), "1. 먼가이상");
    }

    @Test
    @DisplayName("과목 delete 테스트")
    void deleteSubject() {
        DeleteSubjectReq req = new DeleteSubjectReq(1);
    }
}