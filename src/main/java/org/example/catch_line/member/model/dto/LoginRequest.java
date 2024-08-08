package org.example.catch_line.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.catch_line.common.constant.Role;

@AllArgsConstructor
@Getter
@Builder
public class LoginRequest {


    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotNull(message = "역할을 선택해야 합니다.")
    private Role role;


}
