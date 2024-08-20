package org.example.catch_line.user.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.USER.getDescription());

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes: {}", attributes);

        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) properties.get("nickname"); // properties에서 닉네임 가져오기

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email"); // kakao_account에서 이메일 가져오기


        Long kakaoMemberId = ((Number) attributes.get("id")).longValue();


        if (!memberRepository.findByKakaoMemberIdAndIsMemberDeletedFalse(kakaoMemberId).isPresent()
        && !memberRepository.findByEmailAndIsMemberDeletedFalse(new Email(email)).isPresent()
        ) {
            MemberEntity member = MemberEntity.builder()
                    .nickname(nickname)
                    .email(new Email(email))
                    .kakaoMemberId(kakaoMemberId)
                    .build();
            memberRepository.save(member);
        }
        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }
}


