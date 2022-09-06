package ru.practicum.shareit.user;

import java.util.Collection;

/**
 * Интерфейс сервиса пользователей
 */
public interface UserService {
    Collection<UserDto> getUsers();

    UserDto getUser(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto createUser(UserDto userDto);

    void deleteUser(Long userId);

}
