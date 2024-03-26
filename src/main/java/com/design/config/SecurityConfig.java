package com.design.config;

import com.design.filter.CustomAuthenticationEntryPointFilter;
import com.design.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAuthenticationEntryPointFilter customAuthenticationEntryPointFilter;

    private final static String[] PERMIT_ALL = {
            "/v3/**",
            "/swagger-ui/**",
            "/css/**",
            "/js/**",
            "/common/**",
            "/customer",
            "/vendor",
            "/item",
            "/product",
            "/quote",
            "/quote/create",
            "/quote/update/**",
            "/"
    };

    @Bean
    public SecurityFilterChain initSecurity(HttpSecurity http) throws Exception {
        http
                .cors() // 啟用cors過濾器
                .and()
                .csrf(AbstractHttpConfigurer::disable) // CSRF攻擊
                .authorizeHttpRequests( // 認證請求
                        auth -> {
                            auth.requestMatchers(PERMIT_ALL).permitAll();
                            auth.anyRequest().authenticated();
                        })
                .exceptionHandling( // 錯誤處理
                        httpSecurityExceptionHandlingConfigurer -> {
                            httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                                    customAuthenticationEntryPointFilter);
                        })
                .sessionManagement( // Session管理
                        httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))
                .addFilterBefore( // FilterChain
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}