package org.example.catch_line.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.config.auth.MemberDefaultLoginService;
import org.example.catch_line.config.auth.OAuth2LoginService;
import org.example.catch_line.user.token.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MemberJwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberDefaultLoginService memberDefaultLoginService;
    private final OAuth2LoginService oAuth2LoginService;

    private static final List<String> WHITELIST_URLS = Arrays.asList(
            "/login",
            "/signup",
            "/public/**",
            "/static/**",
            "/images/**",
            "/restaurants",
            "/owner",
            "/"
    );

    public MemberJwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, MemberDefaultLoginService principalDetailsService, OAuth2LoginService oAuth2LoginService) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        this.memberDefaultLoginService = principalDetailsService;
        this.oAuth2LoginService = oAuth2LoginService;
    }

    public MemberJwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, JwtTokenUtil jwtTokenUtil, MemberDefaultLoginService principalDetailsService, OAuth2LoginService oAuth2LoginService) {
        super(authenticationManager, authenticationEntryPoint);
        this.jwtTokenUtil = jwtTokenUtil;
        this.memberDefaultLoginService = principalDetailsService;
        this.oAuth2LoginService = oAuth2LoginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();

        if (isWhitelisted(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

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
                Object userDetails = memberDefaultLoginService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication;

                if (userDetails instanceof MemberUserDetails) {
                    MemberUserDetails principalDetail = (MemberUserDetails) userDetails;
                    authentication = new UsernamePasswordAuthenticationToken(principalDetail, null, principalDetail.getAuthorities());
                } else if (userDetails instanceof DefaultOAuth2User) {
                    DefaultOAuth2User oAuth2User = (DefaultOAuth2User) userDetails;
                    authentication = new UsernamePasswordAuthenticationToken(oAuth2User, null, oAuth2User.getAuthorities());
                } else {
                    // 다른 유형의 사용자라면 추가적인 처리를 하지 않고 다음 필터로 넘김
                    chain.doFilter(request, response);
                    return;
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isWhitelisted(String requestURI) {
        return WHITELIST_URLS.stream().anyMatch(url -> requestURI.startsWith(url));
    }
}
