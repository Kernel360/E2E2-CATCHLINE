package org.example.catch_line.user.member.model.mapper;

import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.common.model.vo.Password;
import org.example.catch_line.common.model.vo.PhoneNumber;
import org.example.catch_line.user.member.model.dto.SignUpRequest;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberEntityMapper {

    public MemberEntity toMemberEntity(SignUpRequest signUpRequest, String encodedPassword) {
        return new MemberEntity(new Email(signUpRequest.getEmail()), signUpRequest.getName(), signUpRequest.getNickname(), new Password(encodedPassword), new PhoneNumber(signUpRequest.getPhoneNumber()));
    }

}
