package org.example.catch_line.user.owner.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.model.provider.validation.PasswordValidator;
import org.example.catch_line.user.owner.model.dto.OwnerResponse;
import org.example.catch_line.user.owner.model.dto.OwnerSignUpRequest;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.model.mapper.OwnerResponseMapper;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.example.catch_line.user.owner.validation.OwnerValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerAuthService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;
    private final OwnerValidator  ownerValidator;
    private final OwnerResponseMapper ownerResponseMapper;

    // owner 회원가입
    public OwnerResponse signUp(OwnerSignUpRequest ownerSignUpRequest) {

        // 중복 아이디 검증
        ownerValidator.checkDuplicateLoginId(ownerSignUpRequest.getLoginId());

        String validatedPassword = PasswordValidator.validatePassword(ownerSignUpRequest.getPassword());
        // 검증된 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(validatedPassword);

        // VO에는 암호화된 비밀번호가 넘어간다.
        OwnerEntity owner = new OwnerEntity(ownerSignUpRequest.getLoginId(), ownerSignUpRequest.getName(),
                new Password(encodedPassword), new PhoneNumber(ownerSignUpRequest.getPhoneNumber()));
        ownerRepository.save(owner);

        return ownerResponseMapper.entityToResponse(owner);
    }

}
