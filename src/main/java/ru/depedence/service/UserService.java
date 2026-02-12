package ru.depedence.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.UserContainerDto;
import ru.depedence.entity.dto.UserDto;
import ru.depedence.entity.dto.request.UpdateUserRequest;
import ru.depedence.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public UserContainerDto findAll() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(User::toDto)
                .collect(Collectors.toList());

        return new UserContainerDto(users);
    }

    public UserDto findById(int id) {
        return userRepository.findById(id)
                .map(User::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + id + " not found"));
    }

    public UserDto updateUser(int id, UpdateUserRequest request) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    boolean usernameChanged = false;

                    if (request.username() != null && !request.username().isBlank()) {
                        existingUser.setUsername(request.username());
                        usernameChanged = true;
                    }

                    if (request.password() != null && !request.password().isBlank()) {
                        String hashedPassword = passwordEncoder.encode(request.password());
                        existingUser.setPassword(hashedPassword);
                    }

                    User updatedUser = userRepository.save(existingUser);

                    if (usernameChanged) {
                        updateSecurityContext(updatedUser.getUsername());
                    }

                    return updatedUser.toDto();
                })
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + id + " not found"));
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public void updateSecurityContext(String newUsername) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        if (currentAuth == null) {
            return;
        }

        UserDetails updatedUserDetails = userDetailsService.loadUserByUsername(newUsername);

        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                updatedUserDetails,
                currentAuth.getCredentials(),
                updatedUserDetails.getAuthorities()
        );
        newAuth.setDetails(currentAuth.getDetails());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
