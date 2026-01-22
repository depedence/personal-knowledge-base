package ru.depedence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.depedence.entity.Note;
import ru.depedence.entity.dto.NoteContainerDto;
import ru.depedence.entity.dto.NoteDto;
import ru.depedence.entity.dto.request.CreateNoteRequest;
import ru.depedence.service.NoteService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteRestController {

    private final NoteService noteService;

    @Autowired
    public NoteRestController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes")
    public NoteContainerDto getAll() {
        return noteService.findAll();
    }

    @GetMapping("/notes/{userId}")
    public NoteContainerDto getAllByUserId(@PathVariable int userId) {
        return noteService.findAllByUserId(userId);
    }

    @GetMapping("/note/{noteId}")
    public NoteDto getByNoteId(@PathVariable int noteId) {
        return noteService.findById(noteId);
    }

    @PostMapping("/notes")
    public NoteDto saveNote(@RequestBody CreateNoteRequest request) {
        return noteService.saveNote(request.userId(), request);
    }

    @PutMapping("/notes/{noteId}")
    public NoteDto editNote(@PathVariable int noteId, @RequestBody CreateNoteRequest request) {
        return noteService.editNote(noteId, request);
    }

    @DeleteMapping("/notes/{noteId}")
    public void deleteNote(@PathVariable int noteId) {
        noteService.delete(noteId);
    }

}