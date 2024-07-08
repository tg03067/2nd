package org.example.second.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PatchParentsUserReq {
    @JsonIgnore
    @Schema(description = "학부모 PK")
    private long parentsId;
    @Schema(description = "자녀 이름")
    private String stuNm;
    @Schema(description = "학부모 이름")
    private String nm;
    @Schema(description = "관계")
    private String connet;
    @Schema(description = "학부모 전화번호")
    private String phone;
    @Schema(description = "추가 연락처")
    private String subPhone;
    @Schema(description = "학생 전화번호")
    private String stuPhone;
    @Schema(description = "학부모 email")
    private String email;
    @Schema(description = "자녀 영어 이름")
    private String stuEngNm;
    @Schema(description = "주소")
    private String addr;
    @Schema(description = "우편번호")
    private String zoneCode;
    @Schema(description = "학생 기타사항")
    private String stuEtc;
}
