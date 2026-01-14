package ru.depedence.entity.dto;

import ru.depedence.entity.Note;

import java.util.List;

public class NotesContainerDto {

    private final String userName;
    private final List<Note> notes;
    private final int numberOfDoneNotes;
    private final int numberOfActiveNotes;

    public NotesContainerDto(String userName, List<Note> notes, int numberOfDoneNotes, int numberOfActiveNotes) {
        this.userName = userName;
        this.notes = notes;
        this.numberOfDoneNotes = numberOfDoneNotes;
        this.numberOfActiveNotes = numberOfActiveNotes;
    }

    public String getUserName() {
        return userName;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public int getNumberOfDoneNotes() {
        return numberOfDoneNotes;
    }

    public int getNumberOfActiveNotes() {
        return numberOfActiveNotes;
    }
}