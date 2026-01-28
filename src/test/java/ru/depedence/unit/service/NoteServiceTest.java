package ru.depedence.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.depedence.entity.Note;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.NoteDto;
import ru.depedence.entity.dto.request.CreateNoteRequest;
import ru.depedence.repository.NoteRepository;
import ru.depedence.repository.UserRepository;
import ru.depedence.service.NoteService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoteService Unit Test")
public class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    private User testUser;
    private Note testNote;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setPassword("1234");

        testNote = new Note();
        testNote.setId(1);
        testNote.setTitle("Test Title");
        testNote.setContent("Test Content");
        testNote.setCreationDate(LocalDateTime.now());
        testNote.setUser(testUser);
    }

    @Test
    void findById_IdIsValid__Success() {
        when(noteRepository.findById(testNote.getId())).thenReturn(Optional.of(testNote));

        NoteDto result = noteService.findById(testNote.getId());

        assertNotNull(result);
        assertEquals("Test Title", result.title());
        verify(noteRepository, times(1)).findById(testNote.getId());
    }

    @Test
    void findById_IdIsInvalid__Failure() {
        when(noteRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            noteService.findById(999);
        });
    }

    @Test
    void saveNote_UserExist__Success() {
        CreateNoteRequest request = new CreateNoteRequest("NEW Title", "NEW Content", testUser.getId());

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        NoteDto result = noteService.saveNote(testUser.getId(), request);

        assertNotNull(result);
        verify(userRepository).findById(testUser.getId());
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void saveNote_UserNotExist__Failure() {
        CreateNoteRequest request = new CreateNoteRequest("NEW Title", "NEW Content", 999);

        when(noteRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            noteService.saveNote(999, request);
        });
    }
}