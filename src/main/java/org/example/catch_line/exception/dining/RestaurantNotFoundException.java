package org.example.catch_line.exception.dining;

import org.example.catch_line.exception.CatchLineException;

public class RestaurantNotFoundException extends CatchLineException {

    public RestaurantNotFoundException(Long restaurantId) {
        super("해당하는 식당이 없습니다 : " + restaurantId);
    }
}
