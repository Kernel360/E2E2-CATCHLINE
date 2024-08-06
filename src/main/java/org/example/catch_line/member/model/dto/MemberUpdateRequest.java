package org.example.catch_line.member.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberUpdateRequest {

    @NotBlank
    @Email(message = "올바른 이메일 형식을 입력하세요.")
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하로 입력하세요.")
    private String password;

    @NotBlank
    @Pattern(regexp = "\\d{10,11}", message = "전화번호는 10자 이상 11자 이하의 숫자만 입력하세요.")
    private String phoneNumber;

}
