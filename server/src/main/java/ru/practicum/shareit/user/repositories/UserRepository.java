package ru.practicum.shareit.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

}
