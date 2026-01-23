package ru.depedence.entity.dto;

import java.time.LocalDate;

public record NoteDto(int id, String title, LocalDate creationDate, int userId) {}