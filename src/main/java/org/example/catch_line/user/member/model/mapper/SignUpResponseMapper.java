package org.example.catch_line.user.member.model.mapper;

import org.example.catch_line.user.member.model.dto.SignUpResponse;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class SignUpResponseMapper {

    public SignUpResponse entityToSignUpResponse(MemberEntity member) {
        return SignUpResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail().getEmailValue())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber().getPhoneNumberValue())
                .build();
    }
}

