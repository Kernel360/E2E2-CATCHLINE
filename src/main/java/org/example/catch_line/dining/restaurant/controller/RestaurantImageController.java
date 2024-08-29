package org.example.catch_line.dining.restaurant.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.dining.restaurant.model.entity.RestaurantImageEntity;
import org.example.catch_line.dining.restaurant.service.RestaurantImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RestaurantImageController {

    private final RestaurantImageService restaurantImageService;

    @GetMapping("/images/{restaurantImageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long restaurantImageId) {
        RestaurantImageEntity image = restaurantImageService.getImage(restaurantImageId);

        if (Objects.isNull(image) || Objects.isNull(image.getImageBinaryData())) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFileName() + "\"");
        headers.set(HttpHeaders.CONTENT_TYPE, image.getFileType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(image.getImageBinaryData());
    }


    @GetMapping("/restaurants/{restaurantId}/upload")
    public String upload(Model model, @PathVariable String restaurantId) {
        model.addAttribute("restaurantId", restaurantId);
        return "restaurant/upload-image";
    }

    @PostMapping("/restaurants/{restaurantId}/upload")
    public String uploadImage(@PathVariable Long restaurantId, @RequestPart("image") MultipartFile image, Model model) {
        try {
            // 파일 크기 제한을 체크
            if (image.getSize() > 10485760) { // 예: 10MB 제한
                model.addAttribute("errorMessage", "파일 크기가 너무 큽니다. 10MB 이하의 파일만 업로드 가능합니다.");
                return "restaurant/upload-image";
            }

            RestaurantImageEntity savedImage = restaurantImageService.saveImage(restaurantId, image);
            model.addAttribute("imageId", savedImage.getRestaurantImageId());
            return "redirect:/owner/restaurants/{restaurantId}/edit-images";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "업로드 오류: " + e.getMessage());
            return "restaurant/upload-image";
        }
    }

    @GetMapping("/owner/restaurants/{restaurantId}/edit-images")
    public String editImages(@PathVariable Long restaurantId, Model model) {
        List<RestaurantImageEntity> imageList = restaurantImageService.getImageList(restaurantId);
        model.addAttribute("imageList", imageList);
        model.addAttribute("restaurantId", restaurantId);
        return "owner/edit-images";
    }

    @DeleteMapping("/restaurants/{restaurantId}/images/delete")
    public String deleteImage(@PathVariable Long restaurantId, @RequestParam Long imageId) {
        restaurantImageService.deleteImage(imageId);
        return "redirect:/owner/restaurants/{restaurantId}/edit-images";
    }
}
