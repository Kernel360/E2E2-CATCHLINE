package org.example.catch_line.config.auth;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetail implements UserDetails, OAuth2User {

    private MemberEntity member;

    private Map<String,Object> attributes;

    // 일반 로그인 생성자
    public PrincipalDetail(MemberEntity member) {
        this.member = member;
    }

    // Oauth 로그인 생성자
    public PrincipalDetail(MemberEntity member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 잘 사용하지 않는다.
    @Override
    public String getName() {
        return Strings.EMPTY;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return Role.USER.getDescription();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword() != null ? member.getPassword().getEncodedPassword() : "";
    }

    @Override
    public String getUsername() {
        return member.getEmail().getEmailValue();
    }

}
