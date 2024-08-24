package org.example.catch_line.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.catch_line.config.auth.MemberUserDetails;
import org.example.catch_line.exception.login.LoginException;
import org.example.catch_line.user.token.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;


// 스프링 시큐리티의 필터
// /login 요청해서 username, password post로 전송하면
// 해당 필터가 동작
// security config에서 formLogin disable해서 동작 안함.

@Slf4j
@RequiredArgsConstructor
public class MemberJwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws LoginException {
        log.info("로그인 시도: JwtAuthenticationFilter");

        try {
// BufferedReader를 통해 입력받은 데이터를 문자열로 결합
            BufferedReader br = request.getReader();
            StringBuilder sb = new StringBuilder();
            String input;
            while((input = br.readLine()) != null) {
                sb.append(input);
            }

            // 전체 입력된 데이터
            String requestBody = sb.toString();
            log.info("Raw input: " + requestBody);


            // URL 인코딩된 데이터를 파싱하기 위해 split 사용
            String[] params = requestBody.split("&");
            String username = null;
            String password = null;

            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                    if ("username".equals(key)) {
                        username = value;
                    } else if ("password".equals(key)) {
                        password = value;
                    }
                }
            }

            log.info("Decoded username = " + username);
            log.info("Decoded password = " + password);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);

            // PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
            // 정상이면 authentication이 리턴됨.
            // DB에 있는 username과 password가 일치한다.
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            MemberUserDetails principalDetail = (MemberUserDetails) authentication.getPrincipal();


            log.info("principalDetail Member ID = " + principalDetail.getMember().getEmail().getEmailValue());
            log.info("principalDetail Member Password = " + principalDetail.getMember().getPassword().getEncodedPassword());

            // authentication 객체가 session 영역에 저장됨. -> 로그인이 되었다는 뜻.
            // return 하는 이유는 권한 관리를 security가 대신 해주기 때문에!
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 단지 권한 처리 때문에 session에 넣어준다.

            return authentication;


        } catch (IOException e) {
            throw new LoginException("로그인 실패");
        }




        // 1. username, password 받아서
        // 2. 정상인지 로그인 시도를 해본다.
         /*
         * authenticationManager로 로그인 시도를 하면 PrincipalDetailsService가 호출이 됨
         * loadUserByUserName()이 자동으로 실행된다.
         */
        // 3. PrincipalDetails를 세션에 담고 (세션에 담지 않을 경우 권한 관리가 되지 않는다. 권한 관리 할 필요가 없다면 세션에 담을 필요 없다.)
        // 4. JWT 토큰을 만들어서 응답해주면 된다.
    }



    // attemptAuthentication 실행 후 인증이 정상적으로 되었다면 successfulAuthentication 함수가 실행된다.
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 된다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication 실행, 인증 완료 !!!");

        MemberUserDetails principalDetail = (MemberUserDetails) authResult.getPrincipal();
        String jwtToken = jwtTokenUtil.generateToken(principalDetail.getUsername());

        response.addHeader("Authorization", "Bearer " + jwtToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + jwtToken + "\"}");


        // jwt 토큰을 쿠키에 저장
        Cookie jwtCookie = new Cookie("JWT_TOKEN", jwtToken);
        jwtCookie.setHttpOnly(true);  // XSS 공격 방지
        jwtCookie.setSecure(true);    // HTTPS에서만 사용
        jwtCookie.setPath("/");       // 애플리케이션의 모든 경로에서 사용 가능
        jwtCookie.setMaxAge(600); // 쿠키의 유효기간을 1시간으로 설정 (필요에 따라 조정 가능)

        response.addCookie(jwtCookie);


//        response.sendRedirect("http://localhost:8080/restaurants" + "?token=Bearer "+ jwtToken);

        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // 로그인 실패 시 처리할 로직
        log.error("unsuccessfulAuthentication 실행, 인증 실패: " + failed.getMessage());

        // 에러 메시지를 로그인 페이지로 전달
        response.sendRedirect("/login?error=true&exception=" + java.net.URLEncoder.encode(failed.getMessage(), "UTF-8"));
    }
}
