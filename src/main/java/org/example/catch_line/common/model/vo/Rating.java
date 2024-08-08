package org.example.catch_line.common.model.vo;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class Rating {

    private final BigDecimal rating;

    public Rating(List<Integer> ratings) {
        if(ratings == null || ratings.isEmpty()) {
            this.rating = BigDecimal.ZERO;
            return;
        }
        BigDecimal totalRating = BigDecimal.ZERO;
        for (Integer score : ratings) {
            totalRating = totalRating.add(BigDecimal.valueOf(score));
        }
        this.rating = totalRating.divide(BigDecimal.valueOf(ratings.size()), 1, BigDecimal.ROUND_HALF_UP);
    }

    public Rating(BigDecimal rating) {
        this.rating = rating.setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getRating() {
        return rating;
    }
}
