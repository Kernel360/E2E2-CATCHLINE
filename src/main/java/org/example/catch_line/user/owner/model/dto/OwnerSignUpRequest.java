package org.example.catch_line.user.owner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OwnerSignUpRequest {

    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^[^@]*$", message = "사장님 로그인 아이디는 @ 문자를 포함할 수 없습니다.")
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
