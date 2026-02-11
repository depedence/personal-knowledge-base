package ru.depedence.entity.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.depedence.entity.Note;
import ru.depedence.entity.User;
import ru.depedence.repository.NoteCategory;

import java.time.LocalDateTime;

public record CreateNoteRequest(
        @NotBlank(message = "Title must be a not blank")
        String title,

        @NotBlank(message = "Content must be a not blank")
        String content,

        @NotNull(message = "Category must be a not null")
        @JsonProperty(required = true)
        NoteCategory category,

        int userId
) {

    public Note toEntity(User user) {
        return new Note(title, content, category, LocalDateTime.now(), user);
    }

}