package ru.depedence.entity.dto;

import java.time.LocalDate;

public class NoteDto {

    private final int id;
    private final String title;
    private final LocalDate creationDate;

    public NoteDto(int id, String title, LocalDate creationDate) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
}