package ru.depedence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.depedence.entity.dto.NotesContainerDto;
import ru.depedence.service.NoteService;
import ru.depedence.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final UserService userService;
    private final NoteService noteService;

    @Autowired
    public AccountController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @GetMapping
    public String getMainPage() {
        return "private/account-page.html";
    }

    @PostMapping(value = "/add-note")
    public String addNote(@RequestParam String title) {
        noteService.saveNote(title);
        return "redirect:/account";
    }

}