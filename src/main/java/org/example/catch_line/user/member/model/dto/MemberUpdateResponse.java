package org.example.catch_line.user.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MemberUpdateResponse {

    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
}
