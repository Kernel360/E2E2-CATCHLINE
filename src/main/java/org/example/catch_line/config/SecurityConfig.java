package org.example.catch_line.config;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.config.auth.OAuth2SuccessHandler;
import org.example.catch_line.config.auth.MemberDefaultLoginService;
import org.example.catch_line.config.auth.OAuth2LoginService;
import org.example.catch_line.filter.MemberJwtAuthenticationFilter;
import org.example.catch_line.filter.MemberJwtAuthorizationFilter;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.token.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// secured 어노테이션 사용 가능(특정 메서드에 간단하게 걸고 싶을 때 사용) "ROLE_ADMIN"
// @PreAuthorize 어노테이션 사용 가능 "hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')" 함수가 실행되기 전에 권한을 검사
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final MemberDefaultLoginService memberDefaultLoginService;
    private final OAuth2LoginService oauth2LoginService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberDataProvider memberDataProvider;



    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {   // for hash encrypt
        return new BCryptPasswordEncoder();
    }

    // TODO: AuthenticationManager에 LoginService를 굳이 넣어야 할까?
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberDefaultLoginService)
                .passwordEncoder(bCryptPasswordEncoder());
        return authenticationManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        // form Login disable -> 해당 필터 사용할 수 있도록 추가, AuthenticationManager 넣어줘야 한다.

        MemberJwtAuthenticationFilter memberJwtAuthenticationFilter = new MemberJwtAuthenticationFilter(authenticationManager, jwtTokenUtil);
        MemberJwtAuthorizationFilter memberJwtAuthorizationFilter = new MemberJwtAuthorizationFilter(authenticationManager, jwtTokenUtil, memberDataProvider);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정을 최신 방식으로 변경
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)  // 폼 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP Basic 인증 비활성화
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))  // frameOptions 설정
                .authorizeHttpRequests(requests -> requests
                                .requestMatchers("/**", "/static/**", "/images/**", "/signup", "/login", "/restaurants/**", "/owner/**").permitAll()
                                .requestMatchers("/members/**", "/history/**").hasRole("USER")
//                        .requestMatchers("/owner/restaurants/**").hasRole("OWNER")
                                .anyRequest().permitAll() // 그 외의 요청은 권한 없이 접속 가능
                )

                .addFilter(memberJwtAuthenticationFilter)
                .addFilter(memberJwtAuthorizationFilter)
//                .addFilter(ownerJwtAuthenticationFilter)
//                .addFilter(ownerJwtAuthorizationFilter)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 URL 설정
                        .logoutSuccessUrl("/")
                        .deleteCookies("JWT_TOKEN")// 로그아웃 후 이동할 URL 설정
                        .permitAll()
                )
                // Oauth 로그인
                .oauth2Login(login -> login
                        .loginPage("/login/oauth")
                        .defaultSuccessUrl("/loginSuccess")
                        .successHandler(oAuth2SuccessHandler) // OAuth2 성공 핸들러 설정
                        .userInfoEndpoint()
                        .userService(oauth2LoginService) // OAuth 사용자 로그인 처리
                )
                .userDetailsService(memberDefaultLoginService); // 일반 사용자 로그인 처리
//                .userDetailsService(ownerLoginService); // 식당 사장님 로그인 처리

//                .logout(AbstractHttpConfigurer::disable);  // Spring Security 로그아웃 비활성화

        return http.build();
    }
    // CORS config
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:8081")); // 모든 IP의 응답을 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 모든 종류의 요청을 허용
        configuration.setAllowedHeaders(List.of("*")); // 모든 Header에 응답을 허용
        configuration.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있도록 할지 설정
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Set-Cookie");


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}