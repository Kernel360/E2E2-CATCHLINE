package org.example.catch_line.user.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.catch_line.user.member.model.entity.MemberEntity;

@Getter
@AllArgsConstructor
@Builder
public class SignUpResponse {


    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;


    public static SignUpResponse entityToResponse(MemberEntity member) {
        return SignUpResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail().getEmailValue())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber().getPhoneNumberValue())
                .build();
    }
}
