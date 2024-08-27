package org.example.catch_line.user.owner.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OwnerLoginRequest {

    private String loginId;
    private String password;
}

