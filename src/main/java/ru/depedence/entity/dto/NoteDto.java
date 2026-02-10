package ru.depedence.entity.dto;

import ru.depedence.repository.NoteCategory;

import java.time.LocalDate;

public record NoteDto(
        int id,
        String title,
        String content,
        NoteCategory category,
        LocalDate creationDate,
        int userId
) {}