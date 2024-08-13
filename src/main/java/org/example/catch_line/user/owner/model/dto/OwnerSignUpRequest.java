package org.example.catch_line.user.owner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OwnerSignUpRequest {

    @NotBlank
    private String loginId;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;

    @NotBlank
    @Size(min = 11, max = 14)
    private String phoneNumber;
}
