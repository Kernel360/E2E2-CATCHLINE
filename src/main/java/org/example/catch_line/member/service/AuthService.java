package org.example.catch_line.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.member.model.dto.LoginRequest;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.model.mapper.MemberResponseMapper;
import org.example.catch_line.member.model.vo.Email;
import org.example.catch_line.member.model.vo.PhoneNumber;
import org.example.catch_line.member.repository.MemberRepository;
import org.example.catch_line.member.validate.MemberValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;


    // 회원가입
    public MemberResponse signUp(SignUpRequest signUpRequest) {

        // 이메일 중복 체크에 해당하는 메서드를 따로 만들었습니다. (이유: 회원 수정 시에도 필요)
        memberValidator.checkDuplicateEmail(new Email(signUpRequest.getEmail()));


        MemberEntity member = toMemberEntity(signUpRequest);
        memberRepository.save(member);
        return MemberResponseMapper.entityToResponse(member);


    }


    // 로그인
    public MemberResponse login(LoginRequest loginRequest) {


        return memberRepository.findByEmail(new Email(loginRequest.getEmail()))
                .filter(member -> !member.isMemberDeleted()) // 탈퇴한 회언은 로그인 불가능
                .filter(member -> loginRequest.getPassword().equals(member.getPassword()))
                .map(MemberResponseMapper::entityToResponse)
                .orElseThrow(() -> new IllegalArgumentException("로그인 실패"));


    }


    // 회원가입 요청 `dto`를 `entity`로 변환
    private static MemberEntity toMemberEntity(SignUpRequest signUpRequest) {
        return MemberEntity.builder()
                .email(new Email(signUpRequest.getEmail()))
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .password(signUpRequest.getPassword())
                .phoneNumber(new PhoneNumber(signUpRequest.getPhoneNumber()))
                .role(signUpRequest.getRole())
                .build();
    }


}
