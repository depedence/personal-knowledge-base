package ru.depedence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.depedence.entity.dto.NoteContainerDto;
import ru.depedence.entity.dto.NoteDto;
import ru.depedence.entity.dto.request.CreateNoteRequest;
import ru.depedence.repository.NoteRepository;
import ru.depedence.service.NoteService;

@Controller
@RestController
@RequestMapping("/api")
public class NoteRestController {

    private final NoteService noteService;
    private final NoteRepository noteRepository;

    @Autowired
    public NoteRestController(NoteService noteService, NoteRepository noteRepository) {
        this.noteService = noteService;
        this.noteRepository = noteRepository;
    }

    @GetMapping("/notes")
    public NoteContainerDto findAll() {
        return noteService.findAll();
    }

    @GetMapping("/notes/{id}")
    public NoteDto findById(@PathVariable int id) {
        return noteService.findById(id);
    }

    @PostMapping("/notes")
    public NoteDto saveNote(@RequestBody CreateNoteRequest request) {
        return noteService.saveNote(request);
    }

    @DeleteMapping("/notes/{id}")
    public void deleteNote(@PathVariable int id) {
        noteRepository.deleteById(id);
    }

}