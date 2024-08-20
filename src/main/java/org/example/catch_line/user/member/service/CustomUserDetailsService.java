package org.example.catch_line.user.member.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        MemberEntity member = memberRepository.findByKakaoMemberIdAndIsMemberDeletedFalse(Long.parseLong(username))
                .orElseThrow(() -> new CatchLineException("찾을 수 없는 사용자"));


        // 필요한 경우 사용자의 권한을 설정하고 UserDetails 객체를 반환합니다.
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(member.getKakaoMemberId()),
                "",
                new ArrayList<>()
        );

    }
}
