package org.example.catch_line.dining.restaurant.service;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantImageRepository;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantImageService {

    private final RestaurantImageRepository restaurantImageRepository;
    private final RestaurantValidator restaurantValidator;

    public RestaurantImageEntity saveImage(Long restaurantId, MultipartFile file) throws IOException {
        RestaurantEntity restaurantEntity = restaurantValidator.checkIfRestaurantPresent(restaurantId);
        RestaurantImageEntity image = RestaurantImageEntity.builder()
                .fileName(file.getName())
                .fileType(file.getContentType())
                .imageBinaryData(file.getBytes())
                .restaurant(restaurantEntity)
                .build();

        return restaurantImageRepository.save(image);
    }

    public RestaurantImageEntity getImage(Long restaurantImageId) {
        return restaurantImageRepository.findById(restaurantImageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없음."));
    }

    public List<RestaurantImageEntity> getImageList(Long restaurantId) {
        return restaurantImageRepository.findAllByRestaurantRestaurantId(restaurantId);
    }


}
