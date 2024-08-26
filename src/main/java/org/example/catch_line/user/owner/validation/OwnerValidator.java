package org.example.catch_line.user.owner.validation;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.owner.model.entity.OwnerEntity;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerValidator {

    private final OwnerRepository ownerRepository;

    public OwnerEntity checkIfOwnerIdExist(Long ownerId) {
        return ownerRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new CatchLineException("해당하는 식당 사장님 사용자가 없습니다."));
    }

    public OwnerEntity checkIfOwnerPresent(String loginId) {
        return ownerRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CatchLineException("해당하는 식당 사장님 사용자가 없습니다."));
    }

    public void checkDuplicateLoginId(String loginId) {
        if(ownerRepository.findByLoginId(loginId).isPresent()) {
            throw new CatchLineException("이미 존재하는 식당 사장님 로그인 아이디입니다.");
        }
    }
}
