package org.example.catch_line.exception.dining;

import org.example.catch_line.exception.CatchLineException;

public class RestaurantHourNotFoundException extends CatchLineException {

    public RestaurantHourNotFoundException(Long restaurantHourId) {
        super("영업 시간이 존재하지 않습니다" + restaurantHourId);
    }
}
