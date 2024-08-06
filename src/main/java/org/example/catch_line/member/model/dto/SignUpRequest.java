package org.example.catch_line.member.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.catch_line.common.constant.Role;

@AllArgsConstructor
@Getter
@Builder
public class SignUpRequest {

    @NotNull
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하로 입력하세요.")
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Role role;

}
