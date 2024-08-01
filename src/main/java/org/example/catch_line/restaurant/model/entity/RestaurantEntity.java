package org.example.catch_line.restaurant.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.common.BaseTimeEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    private String name;
    private String description;
    private BigDecimal rating;
    private String phoneNumber;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long scrapCount;
    private Long reviewCount;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

}
