package org.example.catch_line.exception.review;

import org.example.catch_line.exception.CatchLineException;

public class ReviewNotFoundException extends CatchLineException {

    public ReviewNotFoundException(Long reviewId) {
        super("해당하는 리뷰가 없습니다. : " + reviewId);
    }
}
