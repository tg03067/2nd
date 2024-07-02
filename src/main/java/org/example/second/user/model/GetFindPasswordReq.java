package org.example.second.user.model;

import lombok.Data;

@Data
public class GetFindPasswordReq {
    private String uid;
    private String phone;
}
