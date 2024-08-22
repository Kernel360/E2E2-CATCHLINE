package org.example.catch_line.user.member.model.provider.validation;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.email.DuplicateEmailException;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    // TODO: 탈퇴한 회원의 이메일이 db에 남아있음. 이들은 제외하고 이메일 중복 체크 해야 함.
    public void checkDuplicateEmail(Email email) {
        if(memberRepository.findByEmailAndIsMemberDeletedFalse(email).isPresent())
            throw new DuplicateEmailException(email.toString());
    }

    // TODO: 탈퇴한 회원은 제외하고 회원 존재 여부 검사
    public MemberEntity checkIfMemberPresent(Long memberId) {
        return memberRepository.findByMemberIdAndIsMemberDeletedFalse(memberId)
                .orElseThrow(() -> new CatchLineException("해당하는 사용자가 없습니다."));
    }

    public MemberEntity checkIfMemberPresentByEmail(Email email) {
        return memberRepository.findByEmailAndIsMemberDeletedFalse(email)
                .orElseThrow(() -> new CatchLineException("해당하는 사용자가 없습니다."));
    }

    public MemberEntity checkIfKakaoMemberPresent(Long kakaoMemberId) {
        return memberRepository.findByKakaoMemberIdAndIsMemberDeletedFalse(kakaoMemberId)
                .orElseThrow(() -> new CatchLineException("해당하는 카카오 로그인 사용자가 없습니다."));
    }
}
