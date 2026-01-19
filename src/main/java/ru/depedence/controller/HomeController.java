package ru.depedence.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/test")
    public String getHomePage() {
        return "forward:/private/account-page.html";
    }

}