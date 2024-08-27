package org.example.catch_line.config;

import lombok.RequiredArgsConstructor;
import org.example.catch_line.filter.*;
import org.example.catch_line.user.auth.handler.OAuth2SuccessHandler;
import org.example.catch_line.user.auth.service.MemberDefaultLoginService;
import org.example.catch_line.user.auth.service.OAuth2LoginService;
import org.example.catch_line.user.auth.service.OwnerLoginService;
import org.example.catch_line.user.member.model.provider.MemberDataProvider;
import org.example.catch_line.user.auth.token.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
    private final OwnerLoginService ownerLoginService;


    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {   // for hash encrypt
        return new BCryptPasswordEncoder();
    }



    @Bean
    // Primary 지정해버리면 다른 config 파일이더라도 무조건 이것만 사용
    // bean의 이름
    @Primary
    public AuthenticationManager memberAuthenticationManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberDefaultLoginService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public AuthenticationManager ownerAuthenticationManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(ownerLoginService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   @Qualifier("memberAuthenticationManager") AuthenticationManager memberAuthenticationManager,
                                                   @Qualifier("ownerAuthenticationManager") AuthenticationManager ownerAuthenticationManager) throws Exception {

        // form Login disable -> 해당 필터 사용할 수 있도록 추가, AuthenticationManager 넣어줘야 한다.

        MemberJwtAuthenticationFilter memberJwtAuthenticationFilter = new MemberJwtAuthenticationFilter(memberAuthenticationManager, jwtTokenUtil);
        MemberJwtAuthorizationFilter memberJwtAuthorizationFilter = new MemberJwtAuthorizationFilter(memberAuthenticationManager, jwtTokenUtil, memberDataProvider);

        OwnerJwtAuthenticationFilter ownerJwtAuthenticationFilter = new OwnerJwtAuthenticationFilter(ownerAuthenticationManager, jwtTokenUtil);
        OwnerJwtAuthorizationFilter ownerJwtAuthorizationFilter = new OwnerJwtAuthorizationFilter(ownerAuthenticationManager, jwtTokenUtil, ownerLoginService);
        ownerJwtAuthenticationFilter.setFilterProcessesUrl("/owner/login-process");

        RestaurantPreviewFilter restaurantPreviewFilter = new RestaurantPreviewFilter(jwtTokenUtil, memberDataProvider);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 설정을 최신 방식으로 변경
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)  // 폼 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)  // HTTP Basic 인증 비활성화
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))  // frameOptions 설정
                .authorizeHttpRequests(requests -> requests
                                // TODO: url 정확하게 작성
                                .requestMatchers("/members/**", "/history/**").hasRole("USER")
                                .requestMatchers("/reviews/create").hasRole("USER")
                                .requestMatchers(("/reservation")).hasRole("USER")
                                .requestMatchers(("/waiting")).hasRole("USER")
                                .requestMatchers("/owner/restaurants/**").hasRole("OWNER")
                                .anyRequest().permitAll() // 그 외의 요청은 권한 없이 접속 가능
                )
                .addFilter(memberJwtAuthenticationFilter)
                .addFilter(memberJwtAuthorizationFilter)
                .addFilterAfter(restaurantPreviewFilter, memberJwtAuthorizationFilter.getClass())
                .addFilter(ownerJwtAuthenticationFilter)
                .addFilter(ownerJwtAuthorizationFilter)
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
                .userDetailsService(memberDefaultLoginService) // 일반 사용자 로그인 처리
                .userDetailsService(ownerLoginService);
//                .userDetailsService(ownerLoginService); // 식당 사장님 로그인 처리
//                .logout(AbstractHttpConfigurer::disable);  // Spring Security 로그아웃 비활성화
        return http.build();
    }
    // CORS config
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","https://catchline.site","http://localhost:8081"));
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