package org.example.second.user;

import org.apache.ibatis.annotations.Mapper;
import org.example.second.user.model.*;

@Mapper
public interface ParentsUserMapper {
    int postParentsUser(PostParentsUserReq p);
    ParentsUserEntity getParentsUser(GetParentsUserReq parentsId);
    int patchParentsUser(PatchParentsUserReq p);
    GetFindIdRes getFindId(GetFindIdReq req);
    int PatchPassword(PatchPasswordReq req);
}
