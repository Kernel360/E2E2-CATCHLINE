package org.example.catch_line.user.member.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.catch_line.common.constant.Role;

// TODO: No, Setter 타임리프에서 왜  붙여야 하는지? -> 뺐음.
@AllArgsConstructor
@Getter
@Builder
public class LoginRequest {


    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull(message = "역할을 선택해야 합니다.")
    private Role role;


}
