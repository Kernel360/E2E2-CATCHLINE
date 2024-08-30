package org.example.catch_line.dining.restaurant.service;

import org.example.catch_line.dining.restaurant.model.entity.RestaurantEntity;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.dining.restaurant.repository.RestaurantImageRepository;
import org.example.catch_line.dining.restaurant.validation.RestaurantValidator;
import org.example.catch_line.exception.image.ImageNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantImageServiceTest {

    @Mock
    private RestaurantImageRepository restaurantImageRepository;

    @Mock
    private RestaurantValidator restaurantValidator;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private RestaurantImageService restaurantImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveImage() throws IOException {
        Long restaurantId = 1L;
        RestaurantEntity restaurantEntity = mock(RestaurantEntity.class);
        RestaurantImageEntity restaurantImageEntity = mock(RestaurantImageEntity.class);

        when(restaurantValidator.checkIfRestaurantPresent(restaurantId)).thenReturn(restaurantEntity);
        when(multipartFile.getName()).thenReturn("testImage");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(restaurantImageRepository.save(any(RestaurantImageEntity.class))).thenReturn(restaurantImageEntity);

        RestaurantImageEntity savedImage = restaurantImageService.saveImage(restaurantId, multipartFile);

        assertNotNull(savedImage);
        verify(restaurantImageRepository).save(any(RestaurantImageEntity.class));
    }

    @Test
    void testGetImage() {
        Long restaurantImageId = 1L;
        RestaurantImageEntity restaurantImageEntity = mock(RestaurantImageEntity.class);

        when(restaurantImageRepository.findById(restaurantImageId)).thenReturn(Optional.of(restaurantImageEntity));

        RestaurantImageEntity foundImage = restaurantImageService.getImage(restaurantImageId);

        assertNotNull(foundImage);
        verify(restaurantImageRepository).findById(restaurantImageId);
    }

    @Test
    void testGetImageThrowsExceptionWhenNotFound() {
        Long restaurantImageId = 1L;

        when(restaurantImageRepository.findById(restaurantImageId)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> restaurantImageService.getImage(restaurantImageId));
        verify(restaurantImageRepository).findById(restaurantImageId);
    }

    @Test
    void testGetImageList() {
        Long restaurantId = 1L;
        RestaurantImageEntity restaurantImageEntity = mock(RestaurantImageEntity.class);

        when(restaurantImageRepository.findAllByRestaurantRestaurantId(restaurantId)).thenReturn(List.of(restaurantImageEntity));

        List<RestaurantImageEntity> imageList = restaurantImageService.getImageList(restaurantId);

        assertEquals(1, imageList.size());
        verify(restaurantImageRepository).findAllByRestaurantRestaurantId(restaurantId);
    }

    @Test
    void testDeleteImage() {
        Long restaurantImageId = 1L;
        RestaurantImageEntity restaurantImageEntity = mock(RestaurantImageEntity.class);

        when(restaurantImageRepository.findById(restaurantImageId)).thenReturn(Optional.of(restaurantImageEntity));

        restaurantImageService.deleteImage(restaurantImageId);

        verify(restaurantImageRepository).delete(restaurantImageEntity);
    }

    @Test
    void testDeleteImageThrowsExceptionWhenNotFound() {
        Long restaurantImageId = 1L;

        when(restaurantImageRepository.findById(restaurantImageId)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> restaurantImageService.deleteImage(restaurantImageId));
        verify(restaurantImageRepository).findById(restaurantImageId);
    }
}