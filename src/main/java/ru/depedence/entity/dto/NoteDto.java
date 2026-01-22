package ru.depedence.entity.dto;

import java.time.LocalDate;

public class NoteDto {

    private final int id;
    private final String title;
    private final LocalDate creationDate;
    private final int userId;

    public NoteDto(int id, String title, LocalDate creationDate, int userId) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
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