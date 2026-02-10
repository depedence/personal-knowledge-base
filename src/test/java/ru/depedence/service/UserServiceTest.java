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
import ru.depedence.entity.User;
import ru.depedence.entity.dto.UserDto;
import ru.depedence.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@Epic("Unit Testing")
@Feature("User Service")
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Test")
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
    @Story("Find User by Id")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест проверяет, что метод findById возвращает пользователя, когда id пользователя - валидный")
    @DisplayName("findById - find and return test user")
    void findById_UserExist__Success() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        UserDto result = userService.findById(testUser.getId());

        assertNotNull(result);
        assertEquals(1, result.id());
        assertEquals("testUser", result.username());
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    @Story("Find User by Id")
    @Severity(SeverityLevel.NORMAL)
    @Description("Тест проверяет, что метод findById возвращает ошибку, когда id пользователя - невалидный")
    @DisplayName("findById - success return exception")
    void findById_UserNotExist__Failure() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(999));
    }
}