package org.example.catch_line.user.member.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import org.example.catch_line.user.member.model.entity.MemberEntity;

@AllArgsConstructor
@Builder
public class OAuthLoginResponse {

    private Long kakaoMemberId;

    public OAuthLoginResponse entityToResponse(MemberEntity member) {
        return OAuthLoginResponse.builder()
                .kakaoMemberId(member.getKakaoMemberId())
                .build();
    }


}