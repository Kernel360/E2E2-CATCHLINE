package org.example.catch_line.config.auth;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    // 시큐리티 session (내부 Authentication (내부 UserDetails ))
    private final MemberDataProvider memberDataProvider;

    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity member = memberDataProvider.provideMemberByEmail(new Email(username));
        return new PrincipalDetail(member);

    }
}
