package org.example.catch_line.member.model.mapper;

import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.entity.MemberEntity;

public class MemberResponseMapper {

    public static MemberResponse entityToResponse(MemberEntity member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail().getEmailValue())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber().getPhoneNumberValue())
                .role(member.getRole())
                .isMemberDeleted(member.isMemberDeleted())
                .build();
    }
}
