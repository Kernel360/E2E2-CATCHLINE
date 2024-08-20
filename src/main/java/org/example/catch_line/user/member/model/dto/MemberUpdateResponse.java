package org.example.catch_line.user.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.catch_line.user.member.model.entity.MemberEntity;

@Builder
@Getter
@AllArgsConstructor
public class MemberUpdateResponse {

    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;


    // TODO: static 을 뺄 방법
    public static MemberUpdateResponse entityToResponse(MemberEntity member) {
        return MemberUpdateResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail().getEmailValue())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber().getPhoneNumberValue())
                .build();
    }
}
