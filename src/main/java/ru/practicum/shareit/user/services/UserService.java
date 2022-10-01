package ru.practicum.shareit.user.services;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAllUsers();

    UserDto findUserById(Long userId);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto createUser(UserDto userDto);

    void deleteUserById(Long userId);
}
