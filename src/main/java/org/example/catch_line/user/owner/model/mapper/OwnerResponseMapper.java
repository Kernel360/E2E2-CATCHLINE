package org.example.catch_line.user.owner.model.mapper;

import org.example.catch_line.user.owner.model.dto.OwnerResponse;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.springframework.stereotype.Component;


@Component
public class OwnerResponseMapper {

    public OwnerResponse entityToResponse(OwnerEntity owner) {
        return new OwnerResponse(owner.getOwnerId(), owner.getLoginId(), owner.getName(), owner.getPhoneNumber().getPhoneNumberValue());
    }
}
