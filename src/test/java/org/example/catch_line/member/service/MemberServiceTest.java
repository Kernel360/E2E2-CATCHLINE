package org.example.catch_line.member.service;

import org.example.catch_line.common.constant.Role;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MemberServiceTest {


    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    @DisplayName("회원가입 성공 테스트")
    @Test
    void signup_test() {

        // given
        SignUpRequest request = SignUpRequest.builder()
                .email("test2@example.com")
                .name("Test User2")
                .nickname("testnickname2")
                .phoneNumber("01012345678")
                .password("password")
                .role(Role.USER)
                .build();


        memberService.signUp(request);

        MemberEntity savedMember = memberRepository.findByEmail("test2@example.com").orElse(null);
        assertNotNull(savedMember);
        assertEquals(savedMember.getEmail(), request.getEmail());

    }
}