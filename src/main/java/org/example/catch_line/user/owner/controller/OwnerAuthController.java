package org.example.catch_line.user.owner.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.owner.model.dto.OwnerSignUpRequest;
import org.example.catch_line.user.owner.service.OwnerAuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static org.example.catch_line.common.session.SessionConst.*;

@Controller
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerAuthController {

    private final OwnerAuthService ownerAuthService;

    @GetMapping("/login")
    public String showOwnerLoginForm() {
        return "owner/ownerLogin";
    }

    @GetMapping("/signup")
    public String showOwnerSignUpForm(Model model) {
        model.addAttribute("ownerSignUpRequest", new OwnerSignUpRequest(null, null, null, null));
        return "owner/ownerSignup";
    }


    @PostMapping("/signup")
    public String ownerSignup(
            @Valid @ModelAttribute OwnerSignUpRequest ownerSignUpRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bindingResult", bindingResult);
            return "owner/ownerSignup";
        }

        try {
            ownerAuthService.signUp(ownerSignUpRequest);
        } catch (CatchLineException e) {
            model.addAttribute("exception", e.getMessage());
            return "owner/ownerSignup";

        }

        return "redirect:/owner";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    Cookie jwtCookie = new Cookie(cookie.getName(), "");
                    jwtCookie.setPath("/owner"); // 쿠키가 설정된 경로와 동일하게 설정
                    jwtCookie.setMaxAge(0); // 쿠키의 유효기간을 0으로 설정하여 삭제
                    jwtCookie.setHttpOnly(cookie.isHttpOnly()); // 원래 쿠키의 HttpOnly 속성 유지
                    jwtCookie.setSecure(cookie.getSecure()); // 원래 쿠키의 Secure 속성 유지
                    response.addCookie(jwtCookie);
                }
            }
        }

        SecurityContextHolder.clearContext();
        return "redirect:/owner";
    }

}
