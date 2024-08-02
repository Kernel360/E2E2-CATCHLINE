package org.example.catch_line.member.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.member.model.dto.LoginRequest;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.model.mapper.MemberResponseMapper;
import org.example.catch_line.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberResponseMapper memberResponseMapper;

    public MemberResponse signUp(SignUpRequest signUpRequest) {

        if (memberRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }


        MemberEntity member = toMemberEntity(signUpRequest);
        memberRepository.save(member);
        return memberResponseMapper.toDto(member);


    }

    public MemberResponse login(LoginRequest loginRequest) {

        return memberRepository.findByEmail(loginRequest.getEmail())
                .filter(member -> loginRequest.getPassword().equals(member.getPassword()))
                .map(memberResponseMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("로그인 실패"));


    }

    private static MemberEntity toMemberEntity(SignUpRequest signUpRequest) {
        return MemberEntity.builder()
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .password(signUpRequest.getPassword())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .role(signUpRequest.getRole())
                .build();
    }


}
