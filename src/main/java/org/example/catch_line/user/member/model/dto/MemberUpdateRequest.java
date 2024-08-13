package org.example.catch_line.user.member.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberUpdateRequest {

//이메일은 변경 불가
//    @NotBlank
//    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;

    @NotBlank
    @Size(min = 11, max = 14)
    private String phoneNumber;

}
