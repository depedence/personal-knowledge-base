package ru.depedence.entity.dto;

import java.util.List;

public class UserContainerDto {

    private final List<UserDto> users;

    public UserContainerDto(List<UserDto> users) {
        this.users = users;
    }

    public List<UserDto> getUsers() {
        return users;
    }
}