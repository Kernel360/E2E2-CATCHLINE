package org.example.catch_line.user.owner.model.mapper;

import org.example.catch_line.user.owner.model.dto.OwnerResponse;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;

public class OwnerResponseMapper {


    public static OwnerResponse entityToResponse(OwnerEntity owner) {
        return new OwnerResponse(owner.getOwnerId(), owner.getLoginId(), owner.getName(), owner.getPhoneNumber().getPhoneNumberValue());
    }
}
