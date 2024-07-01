package org.example.second.user.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class GetFindIdReq {
    private String nm;
    private String phone;
}
