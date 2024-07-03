package org.example.second.user.model;

import lombok.Data;

@Data
public class UpdateStudentParentsIdReq {
    private long stuId;
    private long parentsId;
}
