package org.example.catch_line.user.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.member.model.dto.MemberDeleteResponse;
import org.example.catch_line.user.member.model.dto.MemberResponse;
import org.example.catch_line.user.member.model.dto.MemberUpdateRequest;
import org.example.catch_line.user.member.model.dto.MemberUpdateResponse;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.mapper.MemberResponseMapper;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.user.member.model.provider.validation.PasswordValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberDataProvider memberDataProvider;

    private final MemberResponseMapper memberResponseMapper;


    // 회원 정보 조회
    public MemberResponse findMember(Long memberId) {
        // TODO: memberDataProvider에서 예외를 체크하고 엔티티를 빌드해서 반환
        MemberEntity member = memberDataProvider.provideMemberByMemberId(memberId);
        return memberResponseMapper.entityToResponse(member);
    }

    // 회원 정보 수정
    public MemberUpdateResponse updateMember(MemberUpdateRequest updateMemberRequest, Long memberId) {
        // 존재하는 회원인지 확인하는 메서드를 따로 추가했습니다. (이유: 회원 탈퇴 시에도 필요)

        MemberEntity member = memberDataProvider.provideMemberByMemberId(memberId);

        PhoneNumber phoneNumber = new PhoneNumber(updateMemberRequest.getPhoneNumber());

        // 회원 수정 dto에 담긴 password 검증
        String validatedPassword = PasswordValidator.validatePassword(updateMemberRequest.getPassword());
        // 검증된 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(validatedPassword);
        // 암호화된 비밀번호로 VO 객체 생성
        Password password = new Password(encodedPassword);

        member.updateMember(updateMemberRequest.getName(), updateMemberRequest.getNickname(), password, phoneNumber);
        return memberResponseMapper.entityToMemberUpdateResponse(member);
    }

    // 회원 탈퇴 (`status`만 변경)
    public MemberDeleteResponse deleteMember(Long memberId) {

        MemberEntity member = memberDataProvider.provideMemberByMemberId(memberId);
        member.doWithdrawal();
        return memberResponseMapper.entityToMemberDeleteResponse(member);
    }


}
