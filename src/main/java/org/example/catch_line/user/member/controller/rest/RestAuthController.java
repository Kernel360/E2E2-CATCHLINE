package org.example.catch_line.user.member.controller.rest;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.constant.Role;
import org.example.catch_line.user.member.model.dto.*;
import org.example.catch_line.user.member.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.example.catch_line.common.session.SessionConst.MEMBER_ID;
import static org.example.catch_line.common.session.SessionConst.ROLE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestAuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(
            @Valid
            @RequestBody SignUpRequest signUpRequest
    ) {

        return ResponseEntity.ok().body(authService.signUp(signUpRequest));

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody LoginRequest loginRequest,
            HttpSession httpSession
    ) {
        LoginResponse loginResponse = authService.login(loginRequest);
        httpSession.setAttribute(MEMBER_ID, loginResponse.getMemberId());
        httpSession.setAttribute(ROLE, Role.USER);

        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/logout")
    public String logout(
        HttpSession httpSession
    ) {
        httpSession.invalidate();
        return "로그아웃 완료";
    }
}
