package org.example.catch_line.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.common.model.vo.Email;
import org.example.catch_line.user.auth.details.MemberUserDetails;
import org.example.catch_line.user.auth.token.JwtTokenUtil;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class RestaurantPreviewFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberDataProvider memberDataProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if(!request.getRequestURI().equals("/restaurants")) {
            chain.doFilter(request, response);
            return;
        }
        log.info("RestaurantPreviewFilter 진입");

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
            chain.doFilter(request, response);
            return; // 필터 체인 진행 중단
        }

        if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if (Objects.nonNull(username) && jwtTokenUtil.validateToken(jwtToken, username)) {

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

}
