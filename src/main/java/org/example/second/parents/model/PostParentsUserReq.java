package org.example.second.parents.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class PostParentsUserReq {
    @JsonIgnore
    private long parentsId;
    @Schema(description = "아이디", required = true)
    private String uid;

    @Schema(description = "비밀번호", required = true)
    private String upw;

    @Schema(description = "이름", required = true)
    private String nm;

    @Schema(description = "전화번호", required = true)
    private String phone;

    @Schema(description = "비상연락망")
    private String subPhone;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "관계", required = true)
    private String connet;

    @Schema(description = "우편번호")
    private String zoneCode ;

    @Schema(description = "주소")
    private String addr;

    @JsonIgnore
    private String addrs ;

    public void setAddrs(String zoneCode, String addr) {
        if(zoneCode == null && addr == null){
            this.addrs = null;
        } else {
            this.addrs = zoneCode + "#" + addr;
        }
    }
}
