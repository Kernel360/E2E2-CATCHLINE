package org.example.catch_line.menu.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.catch_line.restaurant.model.entity.RestaurantEntity;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public void updateMenu(String name, Long price) {
        this.name = name;
        this.price = price;
    }
}
