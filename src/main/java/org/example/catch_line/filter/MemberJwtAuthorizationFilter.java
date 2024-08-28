package org.example.catch_line.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.auth.service.MemberDefaultLoginService;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.auth.token.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MemberJwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberDefaultLoginService memberDefaultLoginService;
    private final MemberDataProvider memberDataProvider;
    private final JwtTokenUtil jwtTokenUtil;

    private static final List<String> WHITELIST_URLS = Arrays.asList(
            "/login",
            "/logout",
            "/signup",
            "/public",
            "/static",
            "/css",
            "/js",
            "/images",
            "/owner"
    );

    private static final List<String> BLACKLIST_URLS = Arrays.asList(
            "/reviews/create",
            "/scraps",
            "/waiting",
            "/reservation"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return WHITELIST_URLS.stream().anyMatch(requestURI::startsWith);
    }

    public MemberJwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, MemberDataProvider memberDataProvider, MemberDefaultLoginService memberDefaultLoginService) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        this.memberDataProvider = memberDataProvider;
        this.memberDefaultLoginService = memberDefaultLoginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/") || (requestURI.startsWith("/restaurants") && !isBlacklisted(requestURI))) {
            chain.doFilter(request, response);
            return;
        }

        log.info("일반 사용자 인증이나 권한이 필요한 주소 요청이 됨.");

        String jwtToken = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (Objects.equals("JWT_TOKEN", cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if(Objects.isNull(jwtToken)) {
            String encodedMessage = URLEncoder.encode("로그인이 필요합니다", StandardCharsets.UTF_8.toString());
            response.sendRedirect("/login?message=" + encodedMessage);
            return; // 필터 체인 진행 중단
        }

        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if (Objects.nonNull(username) && jwtTokenUtil.validateToken(jwtToken, username)) {

                MemberUserDetails memberUserDetails = null;
                if(username.contains("kakao")) {
                    memberUserDetails = new MemberUserDetails(memberDataProvider.provideMemberByKakaoMemberId(username));
                } else {
                    try {
                        memberUserDetails = (MemberUserDetails) memberDefaultLoginService.loadUserByUsername(username);
                    } catch (UsernameNotFoundException e) {
                        response.sendRedirect("/restaurants?authentication-error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
                        return;
                    }
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberUserDetails, null, memberUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isBlacklisted(String requestURI) {

        return BLACKLIST_URLS.stream().anyMatch(url -> {
            if (requestURI.endsWith(url)) return true;
            return false;
        });
    }

}
