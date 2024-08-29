package com.sparta.schedulemanagement.common.config;

import com.sparta.schedulemanagement.common.filter.JwtAuthenticationFilter;
import com.sparta.schedulemanagement.common.util.JwtUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final JwtUtils jwtUtils;

    public FilterConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtUtils));
        registrationBean.addUrlPatterns("/*");  // 모든 경로에 대해 필터를 적용
        return registrationBean;
    }

}
