//package org.example.catch_line.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.catch_line.config.auth.*;
//import org.example.catch_line.user.token.JwtTokenUtil;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//public class OwnerJwtAuthorizationFilter extends BasicAuthenticationFilter {
//
//    private final JwtTokenUtil jwtTokenUtil;
//    private final OwnerLoginService ownerLoginService;
//
//    private static final List<String> WHITELIST_URLS = Arrays.asList(
//            "/login",
//            "/signup",
//            "/public/**",
//            "/static/**",
//            "/images/**",
//            "/restaurants",
//            "/"
//    );
//
//    public OwnerJwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, OwnerLoginService ownerLoginService) {
//        super(authenticationManager);
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.ownerLoginService = ownerLoginService;
//    }
//
//    public OwnerJwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, JwtTokenUtil jwtTokenUtil, OwnerLoginService ownerLoginService) {
//        super(authenticationManager, authenticationEntryPoint);
//        this.jwtTokenUtil = jwtTokenUtil;
//        this.ownerLoginService = ownerLoginService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String requestURI = request.getRequestURI();
//
//        if (isWhitelisted(requestURI)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");
//
//        String jwtToken = null;
//
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if ("JWT_TOKEN".equals(cookie.getName())) {
//                    jwtToken = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
//
//            if (username != null && jwtTokenUtil.validateToken(jwtToken, username)) {
//                OwnerUserDetails ownerUserDetails = (OwnerUserDetails) ownerLoginService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(ownerUserDetails, null, ownerUserDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        chain.doFilter(request, response);
//    }
//
//    private boolean isWhitelisted(String requestURI) {
//        return WHITELIST_URLS.stream().anyMatch(url -> requestURI.startsWith(url));
//    }
//}
