package org.example.catch_line.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.token.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MemberJwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberDataProvider memberDataProvider;
    private final JwtTokenUtil jwtTokenUtil;

    private static final List<String> WHITELIST_URLS = Arrays.asList(
            "/login/**",
            "/logout/**",
            "/signup",
            "/public/**",
            "/static/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/restaurant/**",
            // TODO: 수정 필요
            "/owner/**",
            "/"
    );

    private static final List<String> BLACKLIST_URLS = Arrays.asList(
            "/reviews/create",
            "/scraps",
            "/waiting",
            "/reservation"
    );

    public MemberJwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, MemberDataProvider memberDataProvider) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        this.memberDataProvider = memberDataProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();


        log.info("MemberJwtAuthorizationFilter 진입");

        if (isWhitelisted(requestURI) && !isBlacklisted(requestURI)) {
            log.info("isWhitelisted : {}", isWhitelisted(requestURI));
            log.info("isBlacklisted : {}", isBlacklisted(requestURI));
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

        if(jwtToken == null) {
            String encodedMessage = URLEncoder.encode("로그인이 필요합니다", StandardCharsets.UTF_8.toString());
            response.sendRedirect("/login?message=" + encodedMessage);
            return; // 필터 체인 진행 중단
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if (username != null && jwtTokenUtil.validateToken(jwtToken, username)) {

                MemberUserDetails memberUserDetails;
                if(username.contains("kakao")) {
                    memberUserDetails = new MemberUserDetails(memberDataProvider.provideMemberByKakaoMemberId(username));
                } else {
                    memberUserDetails = new MemberUserDetails(memberDataProvider.provideMemberByEmail(new Email(username)));
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberUserDetails, null, memberUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isBlacklisted(String requestURI) {

        return BLACKLIST_URLS.stream().anyMatch(url -> {
            log.info("url: {}", url);
            log.info("requestURI: {}", requestURI);
            if(requestURI.endsWith(url)) return true;
            return false;
        });
    }

    private boolean isWhitelisted(String requestURI) {
        return WHITELIST_URLS.stream().anyMatch(url -> {
            if (url.endsWith("/**")) {
                return requestURI.startsWith(url.substring(0, url.length() - 3));
            }
            return requestURI.equals(url);
        });
    }

}
