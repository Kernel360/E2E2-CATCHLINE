//package org.example.catch_line.filter;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.catch_line.user.token.JwtTokenUtil;
//import org.springframework.util.PatternMatchUtils;
//
//import java.io.IOException;
//
//@Slf4j
//@RequiredArgsConstructor
//public class LoginCheckFilter implements Filter {
//
//    private static final String[] whitelist = {"/", "/", "/restaurants", "/login", "/loginSuccess", "/login/oauth", "/logout", "/css/*", "/signup", "/check-email", "/restaurants/**", "/owner"};
//    private final JwtTokenUtil jwtTokenUtil;
//
//    @Override
//    public void init(FilterConfig config) throws ServletException {
//        Filter.super.init(config);
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        String requestURI = httpRequest.getRequestURI();
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        try {
//            log.info("JWT 인증 체크 필터 시작 {}", requestURI);
//            if (isLoginCheckPath(requestURI)) {
//                log.info("JWT 인증 체크 로직 실행 {}", requestURI);
//
//                String jwtToken = extractToken(httpRequest);
//
//                // JWT 토큰이 null이거나 빈 문자열인지 확인
//                if (jwtToken == null || jwtToken.trim().isEmpty()) {
//                    log.info("JWT 토큰이 존재하지 않거나 잘못된 토큰입니다. {}", requestURI);
//                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                    return;
//                }
//
//                String email = jwtTokenUtil.getUsernameFromToken(jwtToken);
//
//                if (!jwtTokenUtil.validateToken(jwtToken, email)) {
//                    log.info("유효하지 않은 JWT 토큰 {}", requestURI);
//                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                    return;
//                }
//
//                // JWT 토큰이 유효한 경우, 필요한 추가 작업 수행 가능
//            }
//            chain.doFilter(request, response);
//        } catch (Exception e) {
//            log.error("JWT 인증 체크 중 예외 발생: {}", e.getMessage(), e);
//            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        } finally {
//            log.info("JWT 인증 체크 필터 종료 {}", requestURI);
//        }
//    }
//
//    private String extractToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//    private boolean isLoginCheckPath(String requestURI) {
//        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
//    }
//}
