package ru.depedence.entity.dto.request;

import ru.depedence.entity.Note;
import ru.depedence.entity.User;

import java.time.LocalDateTime;

public record CreateNoteRequest(String title, String content, int userId) {

    public Note toEntity(User user) {
        return new Note(title, content,LocalDateTime.now(), user);
    }

}