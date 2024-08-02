package org.example.catch_line.member.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.member.model.dto.LoginRequest;
import org.example.catch_line.member.model.dto.MemberResponse;
import org.example.catch_line.member.model.dto.SignUpRequest;
import org.example.catch_line.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(
            @Valid
            @RequestBody SignUpRequest signUpRequest
    ) {

        return ResponseEntity.ok().body(memberService.signUp(signUpRequest));

    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
            @Valid
            @RequestBody LoginRequest loginRequest,
            HttpSession httpSession
    ) {
        MemberResponse memberResponse = memberService.login(loginRequest);
        httpSession.setAttribute("memberId", memberResponse.getMemberId());
        httpSession.setAttribute("role", memberResponse.getRole());

        return ResponseEntity.ok().body(memberResponse);

    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }


}
