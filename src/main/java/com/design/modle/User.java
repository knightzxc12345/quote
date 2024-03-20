package com.design.modle;

import com.design.entity.user.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class User implements UserDetails {

    private String uuid;

    private String username;

    private String account;

    private String password;

    private String token;

    private Collection<? extends GrantedAuthority> authorities;

    public User(UserEntity userEntity){
        this.uuid = userEntity.getUuid();
        this.username = userEntity.getName();
        this.account = userEntity.getAccount();
        this.password = userEntity.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
