package org.example.second.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInPostRes {
    @Schema(example = "1", description = "pk")
    private long parentsId;
    @Schema(example = "홍길동", description = "이름")
    private String nm;

    private String accessToken;
}
