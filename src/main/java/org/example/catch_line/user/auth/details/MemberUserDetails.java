package org.example.catch_line.user.auth.details;

import lombok.Data;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class MemberUserDetails implements UserDetails{

    private MemberEntity member;

    // 일반 로그인 생성자
    public MemberUserDetails(MemberEntity member) {
        this.member = member;
    }

    @Override

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.getAuthority()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword().getEncodedPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail().getEmailValue();
    }

}
