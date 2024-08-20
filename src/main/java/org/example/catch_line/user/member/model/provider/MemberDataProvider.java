package org.example.catch_line.user.member.model.provider;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.model.dto.SignUpRequest;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MemberDataProvider {


    private final MemberValidator memberValidator;
    private final MemberRepository memberRepository;

    public MemberEntity provideMemberByMemberId(Long memberId) {
        return memberValidator.checkIfMemberPresent(memberId);
    }

    public MemberEntity provideMemberByKakaoMemberId(Long kakaoMemberId) {
        return memberValidator.checkIfMemberPresent(kakaoMemberId);
    }


    public MemberEntity provideMemberWhenLogin(Email email) {

        return memberValidator.checkIfMemberPresentByEmail(email);
    }

    public void saveMember(MemberEntity member) {
        memberRepository.save(member);
    }


    // TODO:  DTO에 의존해도 될까요?
    public MemberEntity provideMemberWhenSignup(SignUpRequest signUpRequest, String encodedPassword) {

        memberValidator.checkDuplicateEmail(new Email(signUpRequest.getEmail()));

        return MemberEntity.builder()
                .email(new Email(signUpRequest.getEmail()))
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .password(new Password(encodedPassword))
                .phoneNumber(new PhoneNumber(signUpRequest.getPhoneNumber()))
                .build();
    }


}
