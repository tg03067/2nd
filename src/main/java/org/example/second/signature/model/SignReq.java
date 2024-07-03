package org.example.second.signature.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class SignReq {
    private String data;
    private String privateKey;
}
