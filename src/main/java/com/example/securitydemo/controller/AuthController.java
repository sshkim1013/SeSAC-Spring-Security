package com.example.securitydemo.controller;

import com.example.securitydemo.dto.SignupDto;
import com.example.securitydemo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashBoard(
        @AuthenticationPrincipal UserDetails userDetails,
        Model model
    ) {
        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("authorities", userDetails.getAuthorities());
            model.addAttribute("password", userDetails.getPassword());
        }
        return "dashboard";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupDto", new SignupDto());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
        // SignupDto 클래스에 있는 검증 사항(@NotBlank, ...)들을 검증하는 목적.
        @Valid @ModelAttribute SignupDto signupDto,
        // signupDto의 검증 결과를 가지고 있는 객체.
        BindingResult bindingResult
    ) {
        if (!signupDto.getPassword().equals(signupDto.getPasswordConfirm())) {
            // signup.html의 26번째 줄 코드와 같은 변수명(passwordConfirm)이어야 한다.
            bindingResult.rejectValue("passwordConfirm", "mismatch", "비밀번호가 일치하지 않습니다.");
        }

        // 유효성 검사에서 실패(검증 실패)하면 signup.html로 이동
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // DB 조회가 필요한 검증
        // 아이디 중복 체크
        if (userService.existByUsername(signupDto.getUsername())) {
            // signup.html의 16번째 줄 코드와 같은 변수명(username)이어야 한다.
            bindingResult.rejectValue("username", "duplicate", "이미 사용 중인 아이디입니다.");
            return "signup";
        }

        // 이메일 중복 체크
        if (userService.existByEmail(signupDto.getEmail())) {
            // signup.html의 31번째 줄 코드와 같은 변수명(email)이어야 한다.
            bindingResult.rejectValue("email", "duplicate", "이미 사용 중인 이메일입니다.");
            return "signup";
        }

        // 검증 성공 => 회원가입 처리
        userService.register(signupDto);

        return "redirect:/login";
    }

}
