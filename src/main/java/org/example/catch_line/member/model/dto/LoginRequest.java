package org.example.catch_line.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.catch_line.common.constant.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginRequest {


    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 30, message = "비밀번호는 8자 이상, 30자 이하로 입력하세요.")
    private String password;

    @NotNull(message = "역할을 선택해야 합니다.")
    private Role role;


}
