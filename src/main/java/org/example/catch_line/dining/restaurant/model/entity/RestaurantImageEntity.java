package org.example.catch_line.dining.restaurant.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long RestaurantImageId;

    private String fileName;
    private String fileType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBinaryData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    RestaurantEntity restaurant;

    @Builder
    public RestaurantImageEntity(String fileName, String fileType, byte[] imageBinaryData, RestaurantEntity restaurant) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.imageBinaryData = imageBinaryData;
        this.restaurant = restaurant;
    }

}
