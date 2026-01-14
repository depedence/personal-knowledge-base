package ru.depedence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.depedence.entity.User;
import ru.depedence.entity.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByRoleInOrderById(Iterable<UserRole> roles);
    Optional<User> findByUsernameIgnoreCase(String username);

}