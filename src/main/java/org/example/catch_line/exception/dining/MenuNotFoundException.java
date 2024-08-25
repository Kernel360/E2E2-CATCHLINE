package org.example.catch_line.exception.dining;

import org.example.catch_line.exception.CatchLineException;

public class MenuNotFoundException extends CatchLineException {

    public MenuNotFoundException(Long menuId) {
        super("해당 메뉴를 찾을 수 없습니다 : " + menuId);
    }
}
