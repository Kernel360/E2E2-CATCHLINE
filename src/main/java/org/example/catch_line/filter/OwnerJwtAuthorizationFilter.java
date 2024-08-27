package org.example.catch_line.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.auth.details.OwnerUserDetails;
import org.example.catch_line.user.auth.service.OwnerLoginService;
import org.example.catch_line.user.auth.token.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class OwnerJwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final OwnerLoginService ownerLoginService;

    private static final List<String> WHITELIST_URLS = Arrays.asList(
            "/owner/login",
            "/owner/signup",
            "/templates",
            "/static",
            "/css",
            "/js"
    );

    // TODO: 수정
//    private final String[] excludePath

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//
//        String path = request.getRequestURI();
//        return Arrays.stream(excludePath).anyMatch(path::startsWith);
//        return super.shouldNotFilter(request);
//    }

    public OwnerJwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, OwnerLoginService ownerLoginService) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        this.ownerLoginService = ownerLoginService;
    }

    public OwnerJwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, JwtTokenUtil jwtTokenUtil, OwnerLoginService ownerLoginService) {
        super(authenticationManager, authenticationEntryPoint);
        this.jwtTokenUtil = jwtTokenUtil;
        this.ownerLoginService = ownerLoginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();

        log.info("OwnerJwtAuthorizationFilter 동작");
        // Skip the filter for /owner/login and /owner/signup
        if (isWhitelisted(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // Skip the filter for URLs not starting with /owner
        if (!requestURI.startsWith("/owner")) {
            chain.doFilter(request, response);
            return;
        }

        log.info("OwnerJwtAuthorizationFilter 진입");
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");

        String jwtToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if (username != null && jwtTokenUtil.validateToken(jwtToken, username)) {
                OwnerUserDetails ownerUserDetails = (OwnerUserDetails) ownerLoginService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(ownerUserDetails, null, ownerUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }


    private boolean isWhitelisted(String requestURI) {
        return WHITELIST_URLS.stream().anyMatch(url ->  requestURI.startsWith(url));
    }

}
