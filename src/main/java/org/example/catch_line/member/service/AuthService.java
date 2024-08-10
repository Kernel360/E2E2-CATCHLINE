package org.example.catch_line.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.member.model.dto.LoginRequest;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.entity.MemberEntity;
import org.example.catch_line.member.model.mapper.MemberResponseMapper;
import org.example.catch_line.member.model.vo.Email;
import org.example.catch_line.member.model.vo.Password;
import org.example.catch_line.member.model.vo.PhoneNumber;
import org.example.catch_line.member.repository.MemberRepository;
import org.example.catch_line.member.validation.MemberValidator;
import org.example.catch_line.member.validation.PasswordValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;

    private final BCryptPasswordEncoder passwordEncoder;


    // 회원가입
    public MemberResponse signUp(SignUpRequest signUpRequest) {

        // TODO: 컨트롤러에서 한 번 체크하고 있습니다. -> 서비스에는 해당 검증 빼려고 합니다.
        // 이메일 중복 체크에 해당하는 메서드를 따로 만들었습니다. (이유: 회원 수정 시에도 필요)
        memberValidator.checkDuplicateEmail(new Email(signUpRequest.getEmail()));

        // 회원 수정 dto에 담긴 password 검증
        String validatedPassword = PasswordValidator.validatePassword(signUpRequest.getPassword());
        // 검증된 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(validatedPassword);

        log.info("password : {}", encodedPassword);

        // VO에는 암호화된 비밀번호가 넘어간다.
        MemberEntity member = toMemberEntity(signUpRequest, encodedPassword);
        memberRepository.save(member);
        return MemberResponseMapper.entityToResponse(member);


    }


    // 로그인
    public MemberResponse login(LoginRequest loginRequest) {

        return memberRepository.findByEmailAndIsMemberDeletedFalse(new Email(loginRequest.getEmail()))
                .filter(member -> passwordEncoder.matches(loginRequest.getPassword(), member.getPassword().getEncodedPassword())) // 비밀번호 비교
                .filter(member -> loginRequest.getRole().equals(member.getRole()))
                .map(MemberResponseMapper::entityToResponse)
                .orElseThrow(() -> new CatchLineException("로그인에 실패하였습니다.")); // TODO: 기능별 예외 생성

    }


    // 회원가입 요청 `dto`를 `entity`로 변환
    private MemberEntity toMemberEntity(SignUpRequest signUpRequest, String encodedPassword) {

        return MemberEntity.builder()
                .email(new Email(signUpRequest.getEmail()))
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .password(new Password(encodedPassword))
                .phoneNumber(new PhoneNumber(signUpRequest.getPhoneNumber()))
                .role(signUpRequest.getRole())
                .build();
    }


}
