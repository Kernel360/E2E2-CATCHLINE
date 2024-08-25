package org.example.catch_line.config.auth;

import lombok.Data;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class OwnerUserDetails implements UserDetails {

    private OwnerEntity owner;

    public OwnerUserDetails(OwnerEntity owner) {
        this.owner = owner;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.getAuthority()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return owner.getPassword().getEncodedPassword();
    }

    @Override
    public String getUsername() {
        return owner.getLoginId();
    }
}
