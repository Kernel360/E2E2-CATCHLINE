package org.example.catch_line.dining.menu.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @Builder
    public MenuEntity(String name, Long price, RestaurantEntity restaurant) {
        this.name = name;
        this.price = price;
        this.restaurant = restaurant;
    }

    public void updateMenu(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
