package org.example.catch_line.user.member.model.mapper;

import org.example.catch_line.user.member.model.dto.MemberDeleteResponse;
import org.example.catch_line.user.member.model.dto.MemberResponse;
import org.example.catch_line.user.member.model.dto.MemberUpdateResponse;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MemberResponseMapper {

    public MemberUpdateResponse entityToMemberUpdateResponse(MemberEntity member) {
        return MemberUpdateResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail().getEmailValue())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber().getPhoneNumberValue())
                .build();
    }

    public MemberDeleteResponse entityToMemberDeleteResponse(MemberEntity member) {
        return new MemberDeleteResponse(member.getMemberId(), member.isMemberDeleted());
    }

    public  MemberResponse entityToResponse(MemberEntity member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail().getEmailValue())
                .name(Objects.nonNull(member.getName()) ? member.getName() : "")
                .nickname(Objects.nonNull(member.getNickname()) ? member.getNickname() : "")
                .phoneNumber(Objects.nonNull(member.getPhoneNumber())? member.getPhoneNumber().getPhoneNumberValue() : "")
                .isMemberDeleted(member.isMemberDeleted())
                .build();
    }
}
