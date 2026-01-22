package ru.depedence.entity.dto.request;

import ru.depedence.entity.User;

public record CreateUserRequest(String username, String password) {

    public User toEntity() {
        return new User(username, password);
    }
}