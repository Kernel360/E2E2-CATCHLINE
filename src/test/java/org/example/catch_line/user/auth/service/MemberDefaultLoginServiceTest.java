package org.example.catch_line.user.auth.service;

import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MemberDefaultLoginServiceTest {

    @Mock
    private MemberDataProvider memberDataProvider;

    @InjectMocks
    private MemberDefaultLoginService memberDefaultLoginService;

    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock된 MemberEntity 생성
        memberEntity = new MemberEntity(
                new Email("testuser@example.com"),
                "Test User",
                "testNickname",
                null, // password는 필요에 따라 설정
                null  // phoneNumber는 필요에 따라 설정
        );
    }

    @Test
    void loadUserByUsername_UserExists() {
        // given
        when(memberDataProvider.provideMemberByEmail(any(Email.class))).thenReturn(memberEntity);

        // when
        MemberUserDetails userDetails = (MemberUserDetails) memberDefaultLoginService.loadUserByUsername("testuser@example.com");

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("testuser@example.com");
        assertThat(userDetails.getAuthorities()).isNotEmpty();
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        // given
        when(memberDataProvider.provideMemberByEmail(any(Email.class))).thenThrow(new UsernameNotFoundException("User not found"));

        // when / then
        assertThatThrownBy(() -> memberDefaultLoginService.loadUserByUsername("nonexistent@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}
