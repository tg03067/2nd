package org.example.second.signature.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyReq {
    private String data;
    private String signature;
}
