package org.example.catch_line.user.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.member.model.dto.*;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.mapper.SignUpResponseMapper;
import org.example.catch_line.user.member.model.mapper.MemberEntityMapper;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.member.model.provider.validation.PasswordValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SignUpService {

    private final MemberDataProvider memberDataProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberEntityMapper memberEntityMapper;
    private final SignUpResponseMapper signUpResponseMapper;

    // 회원가입
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        // 회원 수정 dto에 담긴 password 검증
        String validatedPassword = PasswordValidator.validatePassword(signUpRequest.getPassword());
        // 검증된 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(validatedPassword);
        // 중복 이메일 체크(중복 시 예외 발생)
        memberDataProvider.provideIfNotDuplicateEmail(new Email(signUpRequest.getEmail()));
        MemberEntity member = memberEntityMapper.toMemberEntity(signUpRequest, encodedPassword);
        memberDataProvider.saveMember(member);

        return signUpResponseMapper.entityToSignUpResponse(member);
    }

}
