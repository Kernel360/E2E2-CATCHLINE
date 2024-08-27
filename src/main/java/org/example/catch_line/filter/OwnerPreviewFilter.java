package org.example.catch_line.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.exception.CatchLineException;
import org.example.catch_line.user.auth.details.OwnerUserDetails;
import org.example.catch_line.user.auth.token.JwtTokenUtil;
import org.example.catch_line.user.owner.repository.OwnerRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OwnerPreviewFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final OwnerRepository ownerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!request.getRequestURI().equals("/owner")) {
            chain.doFilter(request, response);
            return;
        }
        log.info("OwnerPreviewFilter 진입");

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
                OwnerUserDetails ownerUserDetails = new OwnerUserDetails(ownerRepository.findByLoginId(username).orElseThrow(() -> new CatchLineException("로그인하지 않은 식당 사장님입니다.")));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(ownerUserDetails, null, ownerUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
