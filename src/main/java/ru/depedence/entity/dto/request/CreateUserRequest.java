package ru.depedence.entity.dto.request;

import ru.depedence.entity.User;

public class CreateUserRequest {

    private final String username;
    private final String password;

    public CreateUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User toEntity() {
        return new User(
                username,
                password
        );
    }
}