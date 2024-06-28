package org.example.second.user.model;

import lombok.Data;

@Data
public class PatchPasswordReq {
    private long parentsId;
    private String uid;
    private String upw;
    private String newUpw;
}
