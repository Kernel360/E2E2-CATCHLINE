package org.example.catch_line.exception.image;

import org.example.catch_line.exception.CatchLineException;

public class ImageNotFoundException extends CatchLineException {

    public ImageNotFoundException() {
        super("이미지를 찾을 수 없습니다.");
    }
}
