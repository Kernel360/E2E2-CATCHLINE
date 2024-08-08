package org.example.catch_line.member.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.SessionUtils;
import org.example.catch_line.member.model.dto.LoginRequest;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.service.AuthService;
import org.example.catch_line.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.example.catch_line.common.constant.SessionConst.MEMBER_ID;
import static org.example.catch_line.common.constant.SessionConst.ROLE;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(
            @Valid
            @RequestBody SignUpRequest signUpRequest
    ) {

        return ResponseEntity.ok().body(authService.signUp(signUpRequest));

    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
            @Valid
            @RequestBody LoginRequest loginRequest,
            HttpSession httpSession
    ) {
        MemberResponse memberResponse = authService.login(loginRequest);
        httpSession.setAttribute(MEMBER_ID, memberResponse.getMemberId());
        httpSession.setAttribute(ROLE, memberResponse.getRole());

        return ResponseEntity.ok().body(memberResponse);

    }

    @PostMapping("/logout")
    public String logout(
        HttpSession httpSession
    ) {
        httpSession.invalidate();
        return "로그아웃 완료";
    }
}
