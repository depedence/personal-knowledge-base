package ru.depedence.unit.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.UserDto;
import ru.depedence.repository.UserRepository;
import ru.depedence.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setPassword("12345");
    }

    @Test
    void findById_UserExist__Success() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        UserDto result = userService.findById(testUser.getId());

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("testUser", result.username());
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void findById_UserNotExist__Failure() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(999));
    }
}