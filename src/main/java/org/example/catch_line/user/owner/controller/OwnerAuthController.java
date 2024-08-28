package org.example.catch_line.user.owner.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.owner.model.dto.OwnerSignUpRequest;
import org.example.catch_line.user.owner.service.OwnerAuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return "redirect:/owner";
    }

}
