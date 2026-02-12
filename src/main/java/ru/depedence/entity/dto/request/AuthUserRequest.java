package ru.depedence.entity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthUserRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 4, max = 16, message = "Username must be between 4 and 16 characters")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 4, max = 255, message = "Password must be at least 4 characters")
    private String password;

    public AuthUserRequest(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
