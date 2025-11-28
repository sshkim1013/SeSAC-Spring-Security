package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                                    // 로그인에 성공하면 항상 dashboard로 이동
                                   .defaultSuccessUrl("/dashboard", true)
                                   .failureUrl("/login")    // 로그인에 실패 -> 로그인 페이지로 이동
                                   .permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login")
                                    .permitAll());   // 로그아웃에 성공 -> 로그인 페이지로 이동

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailService() {

        // new User("user", "{noop}1234", "USER")
        UserDetails user = User.builder()
            .username("user")
            .password("{noop}1234") // {noop} 의미: 평문으로 저장
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password("{noop}admin")
            .roles("USER", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
