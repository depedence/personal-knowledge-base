package ru.depedence.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String getMainPage(HttpServletRequest request) {
        NotesContainerDto container = noteService.findAllNotes();
        return "private/account-page";
    }



}