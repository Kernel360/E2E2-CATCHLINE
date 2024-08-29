package org.example.catch_line.user.owner.validation;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.user.DuplicateOwnerLoginIdException;
import org.example.catch_line.exception.user.OwnerNotFoundException;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerValidator {

    private final OwnerRepository ownerRepository;

    public OwnerEntity checkIfOwnerIdExist(Long ownerId) {
        return ownerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException(ownerId));
    }

    public void checkDuplicateLoginId(String loginId) {
        if(ownerRepository.findByLoginId(loginId).isPresent()) {
            throw new DuplicateOwnerLoginIdException(loginId);
        }
    }
}
