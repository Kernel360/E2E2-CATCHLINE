package org.example.catch_line.member.service;

import org.example.catch_line.user.member.model.dto.MemberUpdateRequest;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.service.MemberProfileService;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberProfileServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MemberValidator memberValidator;  // MemberValidator를 Mock으로 주입

    @InjectMocks
    private MemberProfileService memberProfileService;

    private MemberEntity defaultMember;

    @BeforeEach
    public void setup() {
        // 실제 Bcrypt 형식의 암호화된 패스워드 생성
        String rawPassword = "1234!@#$qwer";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword); // 실제 암호화된 패스워드 생성

        given(bCryptPasswordEncoder.encode(anyString())).willReturn(encodedPassword);

        defaultMember = MemberEntity.builder()
                .email(new Email("test@gmail.com"))
                .password(new Password(encodedPassword))
                .phoneNumber(new PhoneNumber("010-1212-3434"))
                .nickname("test nickname")
                .name("test name")
                .build();

        given(memberValidator.checkIfMemberPresent(1L))
                .willReturn(defaultMember);
    }

    @DisplayName("회원정보 수정 성공 테스트")
    @Test
    public void member_update_success_test() {

        MemberUpdateRequest memberUpdateRequest =
                MemberUpdateRequest.builder()
                        .name("update test name")
                        .nickname("update test nickname")
                        .password("1234!@#$qwert")
                        .phoneNumber("010-2121-4343")
                        .build();

        memberProfileService.updateMember(memberUpdateRequest, 1L);

        // 검증
        assertEquals("update test name", defaultMember.getName());
        assertEquals("update test nickname", defaultMember.getNickname());
        assertEquals(defaultMember.getPassword().getEncodedPassword(), defaultMember.getPassword().getEncodedPassword());
        assertEquals("010-2121-4343", defaultMember.getPhoneNumber().getPhoneNumberValue());
    }
}
