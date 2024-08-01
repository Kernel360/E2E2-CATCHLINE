package org.example.catch_line.member.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.model.mapper.MemberResponseMapper;
import org.example.catch_line.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberResponseMapper memberResponseMapper;

    public MemberResponse signUp(SignUpRequest signUpRequest) {

        MemberEntity member = MemberEntity.builder()
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .password(signUpRequest.getPassword())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .role(signUpRequest.getRole())
                .build();

        memberRepository.save(member);

        return memberResponseMapper.toDto(member);


    }


}
