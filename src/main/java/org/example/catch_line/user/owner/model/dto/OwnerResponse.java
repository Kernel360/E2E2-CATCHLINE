package org.example.catch_line.user.owner.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OwnerResponse {

    private Long ownerId;
    private String loginId;
    private String name;
    private String phoneNumber;

}
