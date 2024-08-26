package org.example.catch_line.user.member.controller.thymeleaf;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.exception.email.DuplicateEmailException;
import org.example.catch_line.user.member.model.dto.SignUpRequest;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.member.service.AuthService;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberValidator memberValidator;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "member/login";
    }

    // TODO: boolean 말고 response 반환
    @GetMapping("/check-email")
    @ResponseBody // jquery 쓰려면 필요
    public boolean checkEmail(@RequestParam String email) {

        try {
            // TODO: validate 메서드를 controller에서 가져다 쓰는게 맞을까요? OO
            memberValidator.checkDuplicateEmail(new Email(email));
            return true;
        } catch (DuplicateEmailException e) {
            return false;
        }
        // 중복 이메일 예외를 제외하고, 예외가 발생하면 `return` 안함.
    }


    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest(null, null, null, null, null));
        return "member/signup";
    }


    @PostMapping("/signup")
    public String signup(
            @Valid @ModelAttribute SignUpRequest signUpRequest,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            log.info("error : {}", bindingResult);
            model.addAttribute("bindingResult", bindingResult);
            return "member/signup";
        }

        try {
            authService.signUp(signUpRequest);
        } catch (CatchLineException e) {
            model.addAttribute("exception", e.getMessage());
            return "member/signup";

        }

        return "redirect:/";
    }
}
