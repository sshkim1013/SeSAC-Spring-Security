package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/", "/info", "/login", "/h2-console/**", "/signup").permitAll()  // 해당 경로는 로그인 없어도 모두 허용하고
                    .requestMatchers("/admin/**").hasRole("ADMIN")  // ADMIN 권한을 가진 사용자만 접근 가능
                    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // USER, ADMIN 권한을 가진 사용자가 접근 가능
                    .anyRequest().authenticated())  // 나머지 모두는 로그인 필요
            .formLogin(form -> form.loginPage("/login")
                                    // 로그인에 성공하면 항상 dashboard로 이동
                                   .defaultSuccessUrl("/dashboard", true)
                                   .failureUrl("/login")    // 로그인에 실패 -> 로그인 페이지로 이동
                                   .permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login")
                                    .permitAll())   // 로그아웃에 성공 -> 로그인 페이지로 이동
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied")
            );

        httpSecurity.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        httpSecurity.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
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
    */

}
