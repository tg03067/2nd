package org.example.second.scoremaster.exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostExamReq {
    @JsonIgnore
    private long examId;
    @Schema(description = "학기 pk")
    private long semesterId;
    @Schema(description = "과목 pk")
    private long subjectId;
    @Schema(description = "1 -> 중간, 2-> 기말")
    private int option;
}
