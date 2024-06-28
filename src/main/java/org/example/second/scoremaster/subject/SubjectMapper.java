package org.example.second.scoremaster.subject;

import org.apache.ibatis.annotations.Mapper;
import org.example.second.scoremaster.subject.model.DeleteSubjectReq;
import org.example.second.scoremaster.subject.model.GetSubjectRes;
import org.example.second.scoremaster.subject.model.PostSubjectReq;
import org.example.second.scoremaster.subject.model.GetSubjectReq;

import java.util.List;


@Mapper
public interface SubjectMapper {
    int postSubject(PostSubjectReq req);
    int deleteSubject(DeleteSubjectReq req);
    List<GetSubjectRes> getSubjectList(GetSubjectReq req);
}
