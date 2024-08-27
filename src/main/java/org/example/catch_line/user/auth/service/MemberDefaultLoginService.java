package org.example.catch_line.user.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login 로그인 요청이 올 때 동작을 한다.
// Spring Security 기본

@Service
@RequiredArgsConstructor
public class MemberDefaultLoginService implements UserDetailsService {

    // 시큐리티 session (내부 Authentication (내부 UserDetails ))
    private final MemberDataProvider memberDataProvider;

    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity member = memberDataProvider.provideMemberByEmail(new Email(username));
        return new MemberUserDetails(member);
    }
}
