package org.example.catch_line.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginService extends DefaultOAuth2UserService {

    private final MemberDataProvider memberDataProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(Role.USER.getAuthority());

        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes: {}", attributes);

        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String name = (String) properties.get("nickname"); // properties에서 닉네임 가져오기

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email"); // kakao_account에서 이메일 가져오기

        String provider = userRequest.getClientRegistration().getRegistrationId(); // kakao
        Long providerId = ((Number) attributes.get("id")).longValue();
        String nickname = provider + "_" + StringUtils.substring(providerId, 0, 7); // kakao_이름

        if (memberDataProvider.isNotDuplicateKakaoMember(providerId, new Email(email))) {
            MemberEntity member = MemberEntity.builder()
                    .name(name)
                    .nickname(nickname)
                    .email(new Email(email))
                    .kakaoMemberId(providerId)
                    .build();
            memberDataProvider.saveMember(member);
        }

//        MemberEntity member = memberDataProvider.provideMemberByKakaoMemberId(providerId);

        // 어떤 OAuth2 공급자를 통해 로그인하는지, 해당 공급자에서 사용자의 고유 식별자를 나타내는 필드명이 무엇인지를 반환한다.
        // 지금 kakao login만 사용하기 때문에 필요없지만, 추후 구현 위해 남겨 놓는다.
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        log.info("userNameAttributeName: {}", userNameAttributeName);

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }
}


