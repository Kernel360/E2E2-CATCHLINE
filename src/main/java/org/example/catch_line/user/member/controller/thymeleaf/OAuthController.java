package org.example.catch_line.user.member.controller.thymeleaf;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.member.model.entity.MemberEntity;
import org.example.catch_line.user.member.model.provider.validation.MemberValidator;
import org.example.catch_line.user.token.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {
    private final MemberValidator memberValidator;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/login/oauth")
    public String kakaoLogin() {
        return "redirect:/oauth2/authorization/kakao";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal MemberUserDetails principalDetail, Authentication authentication, HttpServletResponse response) {

        if(Objects.isNull(principalDetail)) {
            throw new CatchLineException("Oauth2 user is null");
        }

        MemberEntity member = principalDetail.getMember();
        // JWT 토큰 생성(이메일을 고유 식별자로 사용)
        String jwtToken = jwtTokenUtil.generateToken(member.getEmail().getEmailValue());

        log.info("authentication : " + authentication.getPrincipal());
        // 응답 헤더에 JWT 토큰 추가
        response.setHeader("Authorization", "Bearer " + jwtToken);

        return "redirect:/"; // 로그인 후 리다이렉션할 페이지
    }

}
