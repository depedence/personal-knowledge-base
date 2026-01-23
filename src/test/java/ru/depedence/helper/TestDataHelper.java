package ru.depedence.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.entity.Note;
import ru.depedence.entity.User;
import ru.depedence.repository.NoteRepository;
import ru.depedence.repository.UserRepository;

import java.time.LocalDateTime;

@Component
public class TestDataHelper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    public User createTestUser(String username, String password) {
        User user = new User(username, password);
        return userRepository.save(user);
    }

    public Note createTestNote(String title, User user) {
        Note note = new Note(title, LocalDateTime.now(), user);
        return noteRepository.save(note);
    }

    public void cleanDatabase() {
        noteRepository.deleteAll();
        userRepository.deleteAll();
    }

}