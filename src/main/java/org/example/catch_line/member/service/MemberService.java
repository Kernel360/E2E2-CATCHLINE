package org.example.catch_line.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.member.model.dto.LoginRequest;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.dto.MemberUpdateRequest;
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

        // 이메일 중복 체크에 해당하는 메서드를 따로 만들었습니다. (이유: 회원 수정 시에도 필요)
        checkDuplicateEmail(signUpRequest.getEmail());


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

    // 회원 정보 수정
    public MemberResponse updateMember(MemberUpdateRequest updateMemberRequest, Long memberId) {
        // 존재하는 회원인지 확인하는 메서드를 따로 추가했습니다. (이유: 회원 탈퇴 시에도 필요)
        MemberEntity member = checkIfMemberPresent(memberId);

        // TODO: 해당 부분 리팩토링이 가능한지
        member.updateMember(updateMemberRequest.getEmail(), updateMemberRequest.getName(), updateMemberRequest.getNickname(), updateMemberRequest.getPassword(), updateMemberRequest.getPhoneNumber());


        memberRepository.save(member);
        return memberResponseMapper.toDto(member);
    }

    // 회원 탈퇴 (`status`만 변경)
    public MemberResponse deleteMember(Long memberId) {
        MemberEntity member = checkIfMemberPresent(memberId);

        member.updateMemberStatus(true);
        return memberResponseMapper.toDto(member);


    }

    private MemberEntity checkIfMemberPresent(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사용자가 없습니다."));
    }


    private void checkDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
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
