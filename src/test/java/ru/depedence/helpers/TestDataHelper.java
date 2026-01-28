package ru.depedence.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createTestUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Note createTestNote(String title, User user) {
        Note note = new Note(title, "test content", LocalDateTime.now(), user);
        return noteRepository.save(note);
    }

    public void cleanDatabase() {
        noteRepository.deleteAll();
        userRepository.deleteAll();
    }

}