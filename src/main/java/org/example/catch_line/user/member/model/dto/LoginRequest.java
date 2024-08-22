package org.example.catch_line.user.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

// TODO: No, Setter 타임리프에서 왜  붙여야 하는지? -> 뺐음.
@AllArgsConstructor
@Getter
@Builder
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
