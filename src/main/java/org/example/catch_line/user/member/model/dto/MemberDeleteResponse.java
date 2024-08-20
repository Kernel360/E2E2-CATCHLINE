package org.example.catch_line.user.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.catch_line.user.member.model.entity.MemberEntity;

@AllArgsConstructor
@Builder
@Getter
public class MemberDeleteResponse {


    private Long memberId;
    private boolean isMemberDeleted;


    public static MemberDeleteResponse entityToResponse(MemberEntity member) {
        return MemberDeleteResponse.builder()
                .memberId(member.getMemberId())
                .isMemberDeleted(member.isMemberDeleted())
                .build();
    }
}
