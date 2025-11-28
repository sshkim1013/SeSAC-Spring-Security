package com.example.securitydemo.config;

import com.example.securitydemo.entity.User;
import com.example.securitydemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("1234"))   // 비밀번호 암호화(평문 저장 X)
                .role("ROLE_USER")
                .build();

        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role("ROLE_ADMIN")
                .build();

        userRepository.save(user);
        userRepository.save(admin);

        System.out.println("User 데이터 생성 완료");
    }


}
