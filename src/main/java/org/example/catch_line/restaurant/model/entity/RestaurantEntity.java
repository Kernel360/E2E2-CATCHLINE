package org.example.catch_line.restaurant.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    @Size(max = 5)
    private BigDecimal rating;

    private String phoneNumber;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Column(nullable = false)
    private Long scrapCount;

    @Column(nullable = false)
    private Long reviewCount;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

}
