package org.example.catch_line.config;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.config.auth.PrincipalDetailsService;
import org.example.catch_line.config.auth.PrincipleOAuth2DetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록된다.
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// secured 어노테이션 사용 가능(특정 메서드에 간단하게 걸고 싶을 때 사용) "ROLE_ADMIN"
// @PreAuthorize 어노테이션 사용 가능 "hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')" 함수가 실행되기 전에 권한을 검사
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private final PrincipleOAuth2DetailsService principleOAuth2DetailsService;
    private final PrincipalDetailsService principalDetailsService;


    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {   // for hash encrypt
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PrincipleOAuth2DetailsService principleOAuth2DetailsService) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정을 최신 방식으로 변경
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))  // frameOptions 설정
                .authorizeHttpRequests(requests ->requests
                        .requestMatchers("/**", "/static/**", "/images/**", "/signup", "/login", "/restaurants/**", "/owner").permitAll()
                        .requestMatchers("/members", "/history").authenticated() // 인증이 필요
                        .anyRequest().permitAll()
                )
                // 일반 로그인
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // 로그인 페이지 URL
                        .loginProcessingUrl("/loginProcess")  // 로그인 처리 URL (여기에 POST 요청이 와야 함)
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
        // Oauth 로그인
                .oauth2Login(login -> login
                        .loginPage("/login/oauth")
                        .defaultSuccessUrl("/loginSuccess")
                        .userInfoEndpoint()
                        .userService(principleOAuth2DetailsService)
                )
                .userDetailsService(principalDetailsService);  // 일반 사용자 로그인 처리

//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));



//                .logout(logout -> logout
//                        .logoutUrl("/logout")  // 로그아웃 URL 설정
//                        .logoutSuccessUrl("/")  // 로그아웃 후 이동할 URL 설정
//                        .permitAll()
//                );
//                .logout(AbstractHttpConfigurer::disable);  // Spring Security 로그아웃 비활성화
//        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:8081"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}