package org.example.catch_line.user.owner.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OwnerLoginRequest {

    private String loginId;
    private String password;
}

