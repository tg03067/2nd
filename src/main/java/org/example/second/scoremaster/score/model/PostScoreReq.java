package org.example.second.scoremaster.score.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostScoreReq {
    @JsonIgnore
    private long scoreId;
    @Schema(description = "시험 pk")
    private long examId;
    @Schema(description = "학생 과목 pk")
    private long isuId;
    @Schema(description = "점수")
    private int score;
}
