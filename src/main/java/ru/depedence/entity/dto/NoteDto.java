package ru.depedence.entity.dto;

import java.time.LocalDate;

public record NoteDto(
        int id,
        String title,
        String content,
        LocalDate creationDate,
        int userId
) {}