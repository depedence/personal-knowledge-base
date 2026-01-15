package ru.depedence.controller.exeption;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@ControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    @RequestMapping("/error")
    public String redirectToErrorPage() {
        return "forward:/public/error/error-page.html";
    }

    @ExceptionHandler(Throwable.class)
    public String handleThrowable() {
        return "redirect:/error";
    }

}