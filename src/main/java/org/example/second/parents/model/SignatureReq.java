package org.example.second.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignatureReq {
    @JsonIgnore
    private long signId ;

    @Schema(description = "학생 pk")
    private long stuId ;

    @Schema(description = "년도")
    private int year ;

    @Schema(description = "학기")
    private int semester ;

    @Schema(description = "사인여부")
    private String pic ;
}
