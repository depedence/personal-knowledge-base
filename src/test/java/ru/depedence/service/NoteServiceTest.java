package ru.depedence.service;

import io.qameta.allure.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@Epic("Unit Testing")
@Feature("Note Service")
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
    @Story("Find Note by Id")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет, что метод findById возвращает заметку, когда id заметки - валидный")
    @DisplayName("findById - успешно находит и возвращает заметку")
    void findById_IdIsValid__Success() {
        when(noteRepository.findById(testNote.getId())).thenReturn(Optional.of(testNote));

        NoteDto result = noteService.findById(testNote.getId());

        assertNotNull(result);
        assertEquals("Test Title", result.title());
        verify(noteRepository, times(1)).findById(testNote.getId());
    }

    @Test
    @Story("Find Note by Id")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет, что метод findById возвращает ошибку, когда id заметки - невалидный")
    @DisplayName("findById - успешно обрабатывает ошибку")
    void findById_IdIsInvalid__Failure() {
        when(noteRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> noteService.findById(999));
    }

    @Test
    @Story("Save Note")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Тест проверяет, что метод saveNote сохраняет заметку, когда User - существует")
    @DisplayName("saveNote - успешно сохраняет заметку")
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
    @Story("Save Note")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет, что метод saveNote возвращает ошибку, когда User - не существует")
    @DisplayName("saveNote - успешно обрабатывает ошибку")
    void saveNote_UserNotExist__Failure() {
        CreateNoteRequest request = new CreateNoteRequest("NEW Title", "NEW Content", 999);

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> noteService.saveNote(999, request));
    }

    @Test
    @Story("Edit Note")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет, что метод editNote возвращает отредактированную заметку, когда NoteId - валидно")
    @DisplayName("editNote - успешно редактирует заметку")
    void editNote_NoteExist__Success() {
        CreateNoteRequest request = new CreateNoteRequest("Edit title", "edit content", testUser.getId());

        when(noteRepository.findById(testNote.getId())).thenReturn(Optional.of(testNote));
        when(noteRepository.save(any(Note.class))).thenReturn(testNote);

        NoteDto result = noteService.editNote(testNote.getId(), request);

        assertNotNull(result);
        verify(noteRepository).findById(testNote.getId());
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    @Story("EditNote")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет, что метод editNote возвращает ошибку, когда NoteId - невалидно")
    @DisplayName("editNote - успешно обрабатывает ошибку")
    void editNote_NoteNotExits__Failure() {
        CreateNoteRequest request = new CreateNoteRequest("NEW Title", "NEW Content", testUser.getId());

        when(noteRepository.findById(testNote.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> noteService.editNote(testNote.getId(), request));
    }
}