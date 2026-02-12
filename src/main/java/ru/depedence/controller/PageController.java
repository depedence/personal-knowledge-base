package ru.depedence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login() {
        return "login-page";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration-page";
    }

    @GetMapping("/home")
    public String notesHome() {
        return "home-page";
    }

    @GetMapping("/account")
    public String account() {
        return "account-page";
    }

}
