package org.example.catch_line.member.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.catch_line.common.constant.Role;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SignUpRequest {


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

    @NotNull
    private Role role;

}
