package org.example.catch_line.member.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MemberUpdateRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @NotBlank
    private String phoneNumber;

}
