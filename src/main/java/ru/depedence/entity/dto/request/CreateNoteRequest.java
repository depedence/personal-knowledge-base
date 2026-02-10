package ru.depedence.entity.dto.request;

import ru.depedence.entity.Note;
import ru.depedence.entity.User;
import ru.depedence.repository.NoteCategory;

import java.time.LocalDateTime;

public record CreateNoteRequest(String title, String content, NoteCategory category, int userId) {

    public Note toEntity(User user) {
        return new Note(title, content, category, LocalDateTime.now(), user);
    }

}