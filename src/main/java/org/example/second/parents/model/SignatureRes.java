package org.example.second.parents.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SignatureRes {
    private long signId ;
    private String pics ;
}
