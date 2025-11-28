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
                    .requestMatchers("/", "/info").permitAll()  // '/' 경로와 '/info' 경로는 모두 허용하고
                    .anyRequest().authenticated())  // 나머지는 모두 로그인 필요
            .formLogin(form -> form.permitAll())
            .logout(logout -> logout.permitAll());

        return httpSecurity.build();
    }
}
