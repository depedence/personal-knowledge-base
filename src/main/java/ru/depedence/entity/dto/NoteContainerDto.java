package ru.depedence.entity.dto;

import java.util.List;

public class NoteContainerDto {

    private List<NoteDto> notes;

    public NoteContainerDto(List<NoteDto> notes) {
        this.notes = notes;
    }

    public List<NoteDto> getNotes() {
        return notes;
    }
}