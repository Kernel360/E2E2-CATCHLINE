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
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Role role;

}
