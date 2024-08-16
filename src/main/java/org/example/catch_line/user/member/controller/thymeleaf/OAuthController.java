package org.example.catch_line.user.member.controller.thymeleaf;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestParam(name = "code") @NotBlank  String code) {
    }
    )
}
