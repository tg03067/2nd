package org.example.second.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetParentsUserReq {
    private long signedUserId;
}
