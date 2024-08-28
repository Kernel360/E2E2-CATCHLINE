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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return WHITELIST_URLS.stream().anyMatch(requestURI::startsWith);
    }

    public OwnerJwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, OwnerLoginService ownerLoginService) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        this.ownerLoginService = ownerLoginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();

        // Skip the filter for URLs not starting with /owner
        if (!requestURI.startsWith("/owner") || requestURI.equals("/owner")) {
            chain.doFilter(request, response);
            return;
        }


        log.info("사장님 인증이나 권한이 필요한 주소 요청이 됨.");

        String jwtToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT_TOKEN".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if(Objects.isNull(jwtToken)) {
            String encodedMessage = URLEncoder.encode("식당 사장님 로그인이 필요합니다", StandardCharsets.UTF_8.toString());
            response.sendRedirect("/owner/login?message=" + encodedMessage);
            return; // 필터 체인 진행 중단
        }

        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if (username != null && jwtTokenUtil.validateToken(jwtToken, username)) {
                OwnerUserDetails ownerUserDetails = null;
                try {
                    ownerUserDetails = (OwnerUserDetails) ownerLoginService.loadUserByUsername(username);

                } catch (UsernameNotFoundException e) {
                    response.sendRedirect("/restaurants?authentication-error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
                    return;
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(ownerUserDetails, null, ownerUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }




}
