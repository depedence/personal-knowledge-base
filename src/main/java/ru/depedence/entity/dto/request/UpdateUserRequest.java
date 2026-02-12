package ru.depedence.entity.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 4, max = 16, message = "Username must be between 4 and 16 characters")
        String username,

        @Size(min = 4, max = 255, message = "Password must be at least 4 characters")
        String password
) {}
