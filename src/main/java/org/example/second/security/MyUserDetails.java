package org.example.second.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
public class MyUserDetails implements UserDetails {
    private MyUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override public String getPassword() {return "";}
    @Override public String getUsername() {return "";}
    @Override public boolean isAccountNonExpired() {return false;}
    @Override public boolean isAccountNonLocked() {return false;}
    @Override public boolean isCredentialsNonExpired() {return false;}
    @Override public boolean isEnabled() {return false;}
}
