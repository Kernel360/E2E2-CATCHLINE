package org.example.catch_line.user.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.password.InvalidPasswordException;
import org.example.catch_line.user.member.model.dto.*;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.mapper.MemberResponseMapper;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.member.repository.MemberRepository;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.user.member.model.provider.validation.PasswordValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberDataProvider memberDataProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        // TODO: 컨트롤러에서 한 번 체크하고 있습니다. -> 서비스에는 해당 검증 빼려고 합니다.

        // 회원 수정 dto에 담긴 password 검증
        String validatedPassword = PasswordValidator.validatePassword(signUpRequest.getPassword());
        // 검증된 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(validatedPassword);

        MemberEntity member = memberDataProvider.provideMemberWhenSignup(signUpRequest, encodedPassword);
        memberDataProvider.saveMember(member);

        return SignUpResponse.entityToResponse(member);
    }

    // 로그인
    public LoginResponse login(LoginRequest loginRequest) {

        MemberEntity member = memberDataProvider.provideMemberWhenLogin(new Email(loginRequest.getEmail()));
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword().getEncodedPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        return LoginResponse.entityToResponse(member);
    }





}
