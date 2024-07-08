package org.example.second.parents.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignInPostReq {
    @Schema(example = "test1234", description = "유저 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uid;
    @Schema(example = "Test1234!@#$", description = "유저 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    private String upw;
}
