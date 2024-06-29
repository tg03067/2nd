package org.example.second.user;

import org.example.second.user.model.*;

public interface ParentsUserService {
    int postParentsUser(PostParentsUserReq p);
    ParentsUserEntity getParentsUser(GetParentsUserReq parentsId);
    int patchParentsUser(PatchParentsUserReq p);
    GetFindIdRes getFindId(GetFindIdReq req);
    int patchPassword(PatchPasswordReq req);
}
