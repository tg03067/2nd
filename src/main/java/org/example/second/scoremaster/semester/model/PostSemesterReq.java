package org.example.second.scoremaster.semester.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostSemesterReq {
    @JsonIgnore
    private long semesterId ;

    @Schema(description = "1 -> 1학기, 2 -> 2학기")
    private int option ;
}
