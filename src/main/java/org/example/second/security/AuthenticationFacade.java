package org.example.second.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public MyUser getLoginUser() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return myUserDetails == null ? null : myUserDetails.getUser();
    }
    public long getLoginUserId() {
        MyUser myUser = getLoginUser();
        return myUser == null ? 0 : myUser.getUserId();
    }
}
