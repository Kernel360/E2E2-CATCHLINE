package org.example.catch_line.user.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.auth.token.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;



@Slf4j
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final JwtTokenUtil jwtTokenUtil;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그아웃 이후 request URI: {}", request.getRequestURI());
        String redirectUrl = "/";

        log.info("로그아웃 이후 authentication: {}", authentication);
        String jwtToken = extractJwtFromCookies(request.getCookies());


        response.sendRedirect(redirectUrl);
    }

    private String extractJwtFromCookies(Cookie[] cookies) {
        if (cookies == null) return null;

        Optional<Cookie> jwtCookie = Optional.ofNullable(cookies)
                .flatMap(cookieArray ->
                        Arrays.stream(cookieArray)
                                .filter(cookie -> "JWT_TOKEN".equals(cookie.getName()))
                                .findFirst()
                );

        return jwtCookie.map(Cookie::getValue).orElse(null);
    }
}