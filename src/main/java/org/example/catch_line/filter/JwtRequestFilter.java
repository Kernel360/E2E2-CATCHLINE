//package org.example.catch_line.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.catch_line.exception.CatchLineException;
//import org.example.catch_line.user.token.JwtTokenUtil;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.util.PatternMatchUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    private static final String[] whitelist = {"/", "/restaurants", "/login", "/loginSuccess", "/login/oauth", "/logout", "/css/*", "/signup", "/check-email", "/restaurants/**", "/owner"};
//    private final JwtTokenUtil jwtTokenUtil;
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String requestURI = request.getRequestURI();
//
//        // 화이트리스트에 있는 URL이면 필터를 건너뜀
//        if (!isLoginCheckPath(requestURI)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String username = null;
//        String jwt = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwt = authorizationHeader.substring(7);
//
//            // JWT가 null이거나 빈 문자열인지 확인
//            if (jwt == null || jwt.trim().isEmpty()) {
//                log.warn("JWT Token is null or empty");
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            try {
//                username = jwtTokenUtil.getUsernameFromToken(jwt);
//            } catch (IllegalArgumentException e) {
//                log.warn("Unable to get JWT Token: {}", e.getMessage());
//                filterChain.doFilter(request, response);
//                return;
//            } catch (CatchLineException e) {
//                log.warn("JWT Token has expired: {}", e.getMessage());
//                filterChain.doFilter(request, response);
//                return;
//            }
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            if (jwtTokenUtil.validateToken(jwt, userDetails.getUsername())) {
//                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                log.info("Authentication set in SecurityContextHolder: {}", SecurityContextHolder.getContext().getAuthentication());
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean isLoginCheckPath(String requestURI) {
//        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
//    }
//}
