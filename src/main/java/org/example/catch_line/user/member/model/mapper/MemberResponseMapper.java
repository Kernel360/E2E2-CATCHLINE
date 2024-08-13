package org.example.catch_line.user.member.model.mapper;

import org.example.catch_line.user.member.model.dto.MemberResponse;
import org.example.catch_line.user.member.model.entity.MemberEntity;

public class MemberResponseMapper {

    public static MemberResponse entityToResponse(MemberEntity member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail().getEmailValue())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber().getPhoneNumberValue())
                .isMemberDeleted(member.isMemberDeleted())
                .build();
    }
}
