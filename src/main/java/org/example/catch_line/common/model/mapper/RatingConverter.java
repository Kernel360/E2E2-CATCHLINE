package org.example.catch_line.common.model.mapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.catch_line.common.model.vo.Rating;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Rating rating) {
        return rating.getRating();
    }

    @Override
    public Rating convertToEntityAttribute(BigDecimal rating) {
        return new Rating(rating);
    }
}
