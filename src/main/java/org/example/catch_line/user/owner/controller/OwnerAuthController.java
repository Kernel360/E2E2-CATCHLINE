package org.example.catch_line.user.owner.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.owner.model.dto.OwnerLoginRequest;
import org.example.catch_line.user.owner.model.dto.OwnerResponse;
import org.example.catch_line.user.owner.model.dto.OwnerSignUpRequest;
import org.example.catch_line.user.owner.service.OwnerAuthService;
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
//        model.addAttribute("ownerLoginRequest", new OwnerLoginRequest());
        return "owner/ownerLogin";
    }

    @GetMapping("/signup")
    public String showOwnerSignUpForm(Model model) {
        model.addAttribute("ownerSignUpRequest", new OwnerSignUpRequest());
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

//    @PostMapping("/login")
//    public String ownerLogin(
//            @Valid @ModelAttribute OwnerLoginRequest ownerLoginRequest,
//            BindingResult bindingResult,
//            HttpSession httpSession,
//            Model model
//    ) {
//        if (bindingResult.hasErrors()) {
//            return "owner/ownerLogin";
//        }
//
//        OwnerResponse ownerResponse;
//        try {
//            ownerResponse =
//                    ownerAuthService.login(ownerLoginRequest);
//        } catch (CatchLineException e) {
//            model.addAttribute("exception", e.getMessage());
//            return "owner/ownerLogin";
//        }
//
//        httpSession.setAttribute(OWNER_ID, ownerResponse.getOwnerId());
//        httpSession.setAttribute(ROLE, Role.OWNER);
//
//        return "redirect:/owner";
//    }

    @PostMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/owner";
    }

}
