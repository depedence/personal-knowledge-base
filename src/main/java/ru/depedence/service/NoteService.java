package ru.depedence.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.entity.Note;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.NoteContainerDto;
import ru.depedence.entity.dto.NoteDto;
import ru.depedence.entity.dto.request.CreateNoteRequest;
import ru.depedence.repository.NoteRepository;
import ru.depedence.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public NoteContainerDto findAllByUserId(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + userId + " not found"));
        List<NoteDto> noteDto = user.getNotes().stream()
                .map(Note::toDto)
                .collect(Collectors.toList());

        return new NoteContainerDto(noteDto);
    }

    public NoteDto findById(int id) {
        return noteRepository.findById(id)
                .map(Note::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Note with id = " + id + " not found"));
    }

    public NoteDto saveNote(int userId, CreateNoteRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + userId + " not found"));

        Note note = request.toEntity(user);

        Note savedNote = noteRepository.save(note);

        return savedNote.toDto();
    }

    public NoteDto editNote(int id, CreateNoteRequest request) {
        return noteRepository.findById(id)
                .map(existingNote -> {
                    existingNote.setTitle(request.title());
                    existingNote.setContent(request.content());
                    existingNote.setCategory(request.category());
                    existingNote.setCreationDate(LocalDateTime.now());
                    return noteRepository.save(existingNote).toDto();
                })
                .orElseThrow(() -> new EntityNotFoundException("Note with id = " + id + " not found"));
    }

    public void delete(int id) {
        noteRepository.deleteById(id);
    }

}
