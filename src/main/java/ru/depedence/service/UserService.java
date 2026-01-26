package ru.depedence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.depedence.entity.User;
import ru.depedence.entity.dto.UserContainerDto;
import ru.depedence.entity.dto.UserDto;
import ru.depedence.entity.dto.request.CreateUserRequest;
import ru.depedence.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("User with id = " + id + " not found"));
    }

    public UserDto create(CreateUserRequest request) {
        User user = request.toEntity();
        return userRepository.save(user).toDto();
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

}