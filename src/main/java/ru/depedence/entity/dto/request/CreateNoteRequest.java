package ru.depedence.entity.dto.request;

import ru.depedence.entity.Note;

import java.time.LocalDateTime;

public class CreateNoteRequest {

    private final String title;

    public CreateNoteRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Note toEntity() {
        return new Note(title, LocalDateTime.now());
    }
}