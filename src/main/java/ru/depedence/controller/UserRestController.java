package ru.depedence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.UserContainerDto;
import ru.depedence.entity.dto.UserDto;
import ru.depedence.entity.dto.request.CreateUserRequest;
import ru.depedence.repository.UserRepository;
import ru.depedence.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public UserContainerDto findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserDto createUser(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }

    @GetMapping("/me")
    public UserDto getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user.toDto();
    }

}