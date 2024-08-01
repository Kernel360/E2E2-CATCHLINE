package org.example.catch_line.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.example.catch_line.common.constant.Role;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {


    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private Role role;


}
