package org.example.catch_line.user.member.model.dto;

import lombok.*;
import org.example.catch_line.common.constant.Role;

@AllArgsConstructor
@Getter
@Builder
public class MemberResponse {

    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private Role role;
    private boolean isMemberDeleted;
}
