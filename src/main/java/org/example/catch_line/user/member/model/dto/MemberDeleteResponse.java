package org.example.catch_line.user.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class MemberDeleteResponse {

    private Long memberId;
    private boolean isMemberDeleted;
}
