package org.example.catch_line.common.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public class Rating {

    private final BigDecimal rating;

    public Rating(List<Integer> ratings) {
        if(Objects.isNull(ratings) || ratings.isEmpty()) {
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
