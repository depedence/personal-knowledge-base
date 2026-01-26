package ru.depedence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.NoteContainerDto;
import ru.depedence.entity.dto.NoteDto;
import ru.depedence.entity.dto.request.CreateNoteRequest;
import ru.depedence.repository.UserRepository;
import ru.depedence.service.NoteService;

@RestController
@RequestMapping("/api/notes")
public class NoteRestController {

    private final NoteService noteService;
    private final UserRepository userRepository;

    @Autowired
    public NoteRestController(NoteService noteService, UserRepository userRepository) {
        this.noteService = noteService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public NoteContainerDto getMyNotes() {
        User currentUser = getCurrentUser();
        return noteService.findAllByUserId(currentUser.getId());
    }

    @GetMapping("/{noteId}")
    public NoteDto getNoteById(@PathVariable int noteId) {
        return noteService.findById(noteId);
    }

    @PostMapping
    public NoteDto createNote(@RequestBody CreateNoteRequest request) {
        User currentUser = getCurrentUser();
        return noteService.saveNote(currentUser.getId(), request);
    }

    @PutMapping("/{noteId}")
    public NoteDto editNote(@PathVariable int noteId, @RequestBody CreateNoteRequest request) {
        return noteService.editNote(noteId, request);
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable int noteId) {
        noteService.delete(noteId);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user;
    }

}