package org.example.catch_line.member.service;

import org.example.catch_line.common.constant.Role;
import org.example.catch_line.user.member.model.dto.LoginRequest;
import org.example.catch_line.user.member.model.dto.MemberResponse;
import org.example.catch_line.user.member.model.dto.SignUpRequest;
import org.example.catch_line.user.member.model.vo.Email;
import org.example.catch_line.user.member.model.vo.Password;
import org.example.catch_line.user.member.model.vo.PhoneNumber;
import org.example.catch_line.user.member.service.AuthService;
import org.example.catch_line.user.member.validation.MemberValidator;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private MemberValidator memberValidator;  // MemberValidator를 Mock으로 주입

    @InjectMocks
    private AuthService authService;

    private MemberEntity defaultMember;
    private String encodedPassword;

    @BeforeEach
    public void setup() {

        String rawPassword = "1234!@#$qwer";
        encodedPassword = new BCryptPasswordEncoder().encode(rawPassword); // 실제 암호화된 패스워드 생성

        defaultMember = MemberEntity.builder()
                .email(new Email("test@gmail.com"))
                .password(new Password(encodedPassword))
                .phoneNumber(new PhoneNumber("010-1212-3434"))
                .nickname("test nickname")
                .name("test name")
                .role(Role.USER)
                .build();
    }

    @DisplayName("회원 가입 성공 테스트")
    @Test
    public void member_signup_success_test() {

        // Mock 설정
        given(bCryptPasswordEncoder.encode(anyString())).willReturn(encodedPassword);
        given(memberRepository.save(any(MemberEntity.class))).willReturn(defaultMember);

        ArgumentCaptor<MemberEntity> captor = ArgumentCaptor.forClass(MemberEntity.class);

        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("test@gmail.com")
                .name("test name")
                .nickname("test nickname")
                .password("1234!@#$qwer")
                .phoneNumber("010-1212-3434")
                .role(Role.USER)
                .build();

        // when
        authService.signUp(signUpRequest);

        // then
        verify(memberRepository, times(1)).save(captor.capture());
        MemberEntity savedMember = captor.getValue();

        // 검증
        assertEquals("test@gmail.com", savedMember.getEmail().getEmailValue());
        assertEquals("test name", savedMember.getName());
        assertEquals("test nickname", savedMember.getNickname());
        assertEquals(defaultMember.getPassword().getEncodedPassword(), savedMember.getPassword().getEncodedPassword());
        assertEquals("010-1212-3434", savedMember.getPhoneNumber().getPhoneNumberValue());
        assertEquals(Role.USER, savedMember.getRole());
    }


    @DisplayName("로그인 성공 테스트")
    @Test
    public void member_login_success_test() {
        // Mock 설정
        given(memberRepository.findByEmailAndIsMemberDeletedFalse(any(Email.class)))
                .willReturn(Optional.of(defaultMember));
        given(bCryptPasswordEncoder.matches("1234!@#$qwer", encodedPassword)).willReturn(true);


        // given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("1234!@#$qwer")
                .role(Role.USER)
                .build();


        // when
        MemberResponse response = authService.login(loginRequest);

        // then
        assertEquals("test@gmail.com", response.getEmail());
        assertEquals("test name", response.getName());
        assertEquals("test nickname", response.getNickname());
        assertEquals(Role.USER, response.getRole());
    }

//    @DisplayName("중복 이메일 가입 시 예외 발생 테스트")
//    @Test
//    public void member_signup_duplicate_email_exception_test() {
//
//        // mock 설정
//        given(memberRepository.findByEmailAndIsMemberDeletedFalse(any(Email.class)))
//                .willReturn(Optional.of(defaultMember));
//
//        given(bCryptPasswordEncoder.encode(anyString())).willReturn(encodedPassword);
//
//        // given
//        SignUpRequest signUpRequest = SignUpRequest.builder()
//                .email("test@gmail.com")
//                .name("test name")
//                .nickname("test nickname")
//                .password("1234!@#$qwer")
//                .phoneNumber("010-1212-3434")
//                .role(Role.USER)
//                .build();
//
//
//        // when & then
//        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> {
//            authService.signUp(signUpRequest);
//        });
//
//        // 예외 메시지 검증
//        assertEquals("이미 존재하는 이메일입니다. : test@gmail.com", exception.getMessage());
//
//    }


}


