package org.example.catch_line.user.member.model.dto;

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
    @Size(min=2, max = 10)
    private String name;

    @NotBlank
    @Size(min = 4, max = 15)
    private String nickname;

    @NotBlank
    // thymeleaf 사용으로 인해 회원가입 폼에서는 패스워드 길이를 검증하기 위해 dto 검증을 추가했습니다.
    @Size(min = 8, max = 15)
    private String password;

    @NotBlank
    // thymeleaf 사용 으로 인해 binding result에 dto에 validation을 일부 추가하게 되었습니다.
    @Size(min = 11, max = 14)
    private String phoneNumber;

//    @NotNull
//    private Role role;

}
