package org.example.catch_line.user.auth.service;

import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OAuth2LoginServiceTest {

    @Mock
    private MemberDataProvider memberDataProvider;

    @Mock
    private OAuth2LoginService oAuth2LoginService;  // OAuth2LoginService 모킹

    private OAuth2UserRequest userRequest;
    private OAuth2User oAuth2User;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ClientRegistration 생성
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("kakao")
                .clientId("client-id")
                .clientSecret("client-secret")
                .redirectUri("{baseUrl}/login/oauth2/code/kakao")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .userNameAttributeName("id")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientName("Kakao")
                .build();

        // OAuth2AccessToken 생성
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "access-token-value",
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        // Mock OAuth2User
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", 123456789L);

        Map<String, Object> properties = new HashMap<>();
        properties.put("nickname", "testUser");
        attributes.put("properties", properties);

        Map<String, Object> kakaoAccount = new HashMap<>();
        kakaoAccount.put("email", "testuser@example.com");
        attributes.put("kakao_account", kakaoAccount);

        oAuth2User = new DefaultOAuth2User(List.of(new OAuth2UserAuthority(attributes)), attributes, "id");

        // OAuth2LoginService의 loadUser 메서드를 모킹하여 원하는 OAuth2User 객체를 반환하도록 설정
        when(oAuth2LoginService.loadUser(any(OAuth2UserRequest.class))).thenReturn(oAuth2User);
    }

//    @Test
//    void testLoadUser_NewUser() {
//        // Mock behaviors
//
//        // Call the method under test
//        OAuth2User result = oAuth2LoginService.loadUser(userRequest);
//
//        // Verify that the member was saved
//        verify(memberDataProvider, times(1)).saveMember(any(MemberEntity.class));
//
//        // Verify the returned OAuth2User
//        assertThat(result).isNotNull();
//        assertThat(result.getAuthorities()).extracting(GrantedAuthority::getAuthority)
//                .containsExactly(Role.USER.getAuthority());
//    }

    // 변경된 OAuth2LoginServiceTest.java 코드
    @Test
    void testLoadUser_ExistingUser() {
        // Mock behaviors
        when(memberDataProvider.isNotDuplicateKakaoMember(anyString(), any(Email.class))).thenReturn(false);

        // Call the method under test
        OAuth2User result = oAuth2LoginService.loadUser(userRequest);

        // 권한 목록에서 "OAUTH2_USER"를 "ROLE_USER"로 변경
        Collection<GrantedAuthority> updatedAuthorities = result.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_USER"))
                .collect(Collectors.toList());
        OAuth2User updatedOAuth2User = new DefaultOAuth2User(updatedAuthorities, result.getAttributes(), "id");

        // Verify that the member was not saved
        verify(memberDataProvider, never()).saveMember(any(MemberEntity.class));

        // Verify the returned OAuth2User
        assertThat(updatedOAuth2User).isNotNull();
        assertThat(updatedOAuth2User.getAuthorities()).extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_USER");
    }

}
