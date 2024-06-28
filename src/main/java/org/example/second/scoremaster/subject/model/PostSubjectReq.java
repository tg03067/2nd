package org.example.second.scoremaster.subject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostSubjectReq {
    @JsonIgnore
    private long subjectId;
    @Schema(description = "과목명")
    private String subjectName;
    @Schema(description = "학년")
    private long grade;
}
