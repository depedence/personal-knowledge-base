package ru.depedence.entity.dto.request;

import ru.depedence.entity.Note;
import ru.depedence.entity.User;

import java.time.LocalDateTime;

public record CreateNoteRequest(String title, int userId) {

    public Note toEntity(User user) {
        return new Note(title, LocalDateTime.now(), user);
    }

    public Note toEntity(int id, LocalDateTime creationDate, User user) {
        return new Note(title, creationDate, user);
    }

    public Note toEntity(int id, LocalDateTime creationDate) {
        return new Note(title, creationDate);
    }

}