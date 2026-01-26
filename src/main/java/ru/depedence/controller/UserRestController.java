package ru.depedence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.depedence.entity.dto.UserContainerDto;
import ru.depedence.entity.dto.UserDto;
import ru.depedence.entity.dto.request.CreateUserRequest;
import ru.depedence.repository.UserRepository;
import ru.depedence.service.UserService;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public UserContainerDto findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public UserDto findById(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    public UserDto createUser(@RequestBody CreateUserRequest request) {
        return userService.create(request);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.delete(id);
    }

}