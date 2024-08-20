package org.example.catch_line.user.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.catch_line.user.member.model.entity.MemberEntity;

@AllArgsConstructor
@Builder
@Getter
public class LoginResponse {

    private Long memberId;


    public static LoginResponse entityToResponse(MemberEntity member) {
        return LoginResponse.builder()
                .memberId(member.getMemberId())
                .build();
    }


}
