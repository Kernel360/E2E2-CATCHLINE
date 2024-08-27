package org.example.catch_line.user.auth.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.catch_line.user.auth.token.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // 이메일로 토큰 생성
//        MemberEntity member = memberDataProvider.provideMemberByKakaoMemberId(Long.valueOf(authentication.getName()));
//        String jwtToken = jwtTokenUtil.generateToken( member.getEmail().getEmailValue());

        // 카카오 아이디로 토큰 생성
        String jwtToken = jwtTokenUtil.generateToken("kakao_" + authentication.getName());

        // JWT 토큰을 쿠키에 저장
        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(600); // 쿠키 유효기간 설정

        response.addCookie(jwtCookie);

        // 로그인 성공 후 리다이렉트
        response.sendRedirect("/restaurants");
    }
}
