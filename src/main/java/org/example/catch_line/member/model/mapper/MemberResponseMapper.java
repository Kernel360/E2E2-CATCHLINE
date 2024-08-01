package org.example.catch_line.member.model.mapper;

import lombok.AllArgsConstructor;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.springframework.stereotype.Service;

@Service
public class MemberResponseMapper {

    public MemberResponse toDto(MemberEntity member) {
        return MemberResponse.builder()
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole())
                .build();
    }
}
