package ru.depedence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.entity.Note;
import ru.depedence.entity.dto.NoteContainerDto;
import ru.depedence.entity.dto.NoteDto;
import ru.depedence.entity.dto.request.CreateNoteRequest;
import ru.depedence.repository.NoteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteContainerDto findAll() {
        List<NoteDto> notes = noteRepository.findAll().stream()
                .map(Note::toDto)
                .collect(Collectors.toList());

        return new NoteContainerDto(notes);
    }

    public NoteDto findById(int id) {
        return noteRepository.findById(id)
                .map(Note::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Note with id = " + id + " not found"));
    }

    public NoteDto saveNote(CreateNoteRequest request) {
        Note note = request.toEntity();
        return noteRepository.save(note).toDto();
    }


}