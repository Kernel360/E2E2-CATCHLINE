package org.example.catch_line.exception.user;

import org.example.catch_line.exception.CatchLineException;

public class OwnerNotFoundException extends CatchLineException {

    public OwnerNotFoundException(Long ownerId) {
        super("해당하는 식당 사장님 사용자가 없습니다 : " + ownerId);
    }

}
