package org.example.catch_line.user.owner.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OwnerResponse {

    private Long ownerId;
    private String loginId;
    private String name;
    private String phoneNumber;

}
