package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/info", "/login").permitAll()  // '/' 경로와 '/info' 경로는 모두 허용하고
                    .anyRequest().authenticated())  // 나머지는 모두 로그인 필요
            .formLogin(form -> form.loginPage("/login")
                                   .defaultSuccessUrl("/dashboard", true)
                                   .failureUrl("/login")
                                   .permitAll())    // 로그인에 성공하면 항상 dashboard로 이동
            .logout(logout -> logout.logoutSuccessUrl("/login")
                                    .permitAll());   // 로그아웃에 성공 -> 로그인 페이지로 이동

        return httpSecurity.build();
    }
}
