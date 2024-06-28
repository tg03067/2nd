package org.example.second.scoremaster.student_subject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostStudentSubjectReq {
    @JsonIgnore
    private long isuId;
    @Schema(description = "학생 pk")
    private long stuId;
    @Schema(description = "과목 pk")
    private long subjectId;
    @Schema(description = "학기 pk")
    private long semesterId;
}
