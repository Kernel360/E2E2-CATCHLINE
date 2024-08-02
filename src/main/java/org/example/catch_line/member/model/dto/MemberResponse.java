package org.example.catch_line.member.model.dto;

import lombok.*;
import org.example.catch_line.common.constant.Role;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MemberResponse {


    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String phoneNumber;
    private Role role;
}
