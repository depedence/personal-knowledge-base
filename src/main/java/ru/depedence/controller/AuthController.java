package ru.depedence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.depedence.entity.User;
import ru.depedence.entity.UserRole;
import ru.depedence.service.UserService;

import java.util.Collections;
import java.util.Set;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(required = false) String error) {
        if (error != null) {
            model.addAttribute("isAuthFailed", true);
        }

        return "forward:/public/auth/login-page.html";
    }

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "forward:/public/auth/registration-page.html";
    }

    @PostMapping("/registration")
    public String createUserAccount(@RequestParam String username, @RequestParam String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userService.save(new User(username, encodedPassword, UserRole.USER));
        forceAutoLogin(username, password);
        return "redirect:/account.html";
    }

    private void forceAutoLogin(String username, String password) {
        Set<SimpleGrantedAuthority> roles = Collections.singleton(UserRole.USER.toAuthority());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password, roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}