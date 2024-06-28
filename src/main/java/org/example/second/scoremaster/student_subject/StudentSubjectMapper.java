package org.example.second.scoremaster.student_subject;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentSubjectMapper {
    int postScore();
}
