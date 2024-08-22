//package org.example.catch_line.config;
//
//import jakarta.servlet.Filter;
//import org.example.catch_line.filter.LoginCheckFilter;
//import org.example.catch_line.user.token.JwtTokenUtil;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public FilterRegistrationBean loginCheckFilter(JwtTokenUtil jwtTokenUtil) {
//        FilterRegistrationBean<Filter> filterRegistrationBean = new
//                FilterRegistrationBean<>();
//        filterRegistrationBean.setFilter((Filter) new LoginCheckFilter(jwtTokenUtil));
//        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
//        filterRegistrationBean.addUrlPatterns("/*");
//        return filterRegistrationBean;
//    }
//}
