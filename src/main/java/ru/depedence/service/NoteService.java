package ru.depedence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.entity.Note;
import ru.depedence.entity.NoteStatus;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.NotesContainerDto;
import ru.depedence.repository.NoteRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final UserService userService;
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(UserService userService, NoteRepository noteRepository) {
        this.userService = userService;
        this.noteRepository = noteRepository;
    }

    @Transactional(readOnly = true)
    public NotesContainerDto findAllNotes() {
        User user = userService.getCurrentUser();

        List<Note> notes = user.getNotes().stream()
                .sorted(Comparator.comparingInt(Note::getId))
                .collect(Collectors.toList());

        int numberOfDoneNotes = (int) notes.stream().filter(record -> record.getStatus() == NoteStatus.DONE).count();
        int numberOfActiveNotes = (int) notes.stream().filter(record -> record.getStatus() == NoteStatus.ACTIVE).count();

        return new NotesContainerDto(user.getUsername(), notes, numberOfDoneNotes, numberOfActiveNotes);
    }

    public void saveNote(String title) {
        if (title != null && !title.isBlank()) {
            User user = userService.getCurrentUser();
            noteRepository.save(new Note(title, user));
        }
    }

    public void updateNoteStatus(int id, NoteStatus newStatus) {
        noteRepository.update(id, newStatus);
    }

    public void deleteNote(int id) {
        noteRepository.deleteById(id);
    }
}