package org.example.second.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PatchPasswordReq {
    @JsonIgnore
    private long parentsId;

    private String uid;
    private String upw;
    private String newUpw;
}
