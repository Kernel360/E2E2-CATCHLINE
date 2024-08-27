package org.example.catch_line.common.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.token.JwtTokenUtil;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtils {

    private final JwtTokenUtil jwtTokenUtil;


    private String extractTokenFromCookie(HttpServletRequest request) {

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (Objects.equals("JWT_TOKEN", cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new CatchLineException("쿠키에 JWT TOKEN이 없습니다.");
    }

    public String validateUserByToken(HttpServletRequest request) {
        String jwtToken = extractTokenFromCookie(request);
        jwtTokenUtil.validateToken(jwtToken, jwtTokenUtil.getUsernameFromToken(jwtToken));
        return jwtTokenUtil.getUsernameFromToken(jwtToken);
    }
}
