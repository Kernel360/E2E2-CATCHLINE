package org.example.catch_line.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.restaurant.service.RestaurantImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RestaurantImageController {

    private final RestaurantImageService restaurantImageService;

    // 이미지 불러오는 메서드
    @GetMapping("/images/{restaurantImageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long restaurantImageId) {
        RestaurantImageEntity image = restaurantImageService.getImage(restaurantImageId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, image.getFileType())
                .body(image.getImageBinaryData());
    }

    // 업로드 페이지
    @GetMapping("/restaurants/{restaurantId}/upload")
    public String upload(Model model, @PathVariable String restaurantId) {
        model.addAttribute("restaurantId", restaurantId);
        return "restaurant/upload-image";
    }

    // 업로드
    @PostMapping("/restaurants/{restaurantId}/upload")
    public String uploadImage(@PathVariable Long restaurantId, @RequestPart("image") MultipartFile image, Model model) {
        try {
            RestaurantImageEntity savedImage = restaurantImageService.saveImage(restaurantId, image);
            model.addAttribute("imageId", savedImage.getRestaurantImageId());
            return "redirect:/restaurants/" + savedImage.getRestaurant().getRestaurantId();
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "업로드 오류: " + e.getMessage());
            return "restaurant/upload-image";
        }
    }
}
