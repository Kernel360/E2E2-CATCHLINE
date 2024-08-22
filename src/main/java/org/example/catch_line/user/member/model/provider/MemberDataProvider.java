package org.example.catch_line.user.member.model.provider;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.model.vo.Email;
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

    public MemberEntity provideMemberByEmail(Email email) {
        return memberValidator.checkIfMemberPresentByEmail(email);
    }

    public MemberEntity provideMemberByKakaoMemberId(Long kakaoMemberId) {
        return memberValidator.checkIfKakaoMemberPresent(kakaoMemberId);
    }


    public MemberEntity provideMemberWhenLogin(Email email) {
        return memberValidator.checkIfMemberPresentByEmail(email);
    }

    public void saveMember(MemberEntity member) {
        memberRepository.save(member);
    }


    public void provideIfNotDuplicateEmail(Email email) {
        memberValidator.checkDuplicateEmail(email);
    }


    public boolean isNotDuplicateKakaoMember(Long kakaoMemberId, Email email) {
        return memberRepository.findByKakaoMemberIdAndIsMemberDeletedFalse(kakaoMemberId).isEmpty()
                &&
                memberRepository.findByEmailAndIsMemberDeletedFalse(email).isEmpty();
    }
}
