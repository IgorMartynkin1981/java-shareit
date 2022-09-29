package ru.practicum.shareit.user.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repositories.UserRepository;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long userId) {
        return UserMapper.toUserDto(findAndValidationUserInRepositiry(userId));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = findAndValidationUserInRepositiry(userId);

        return UserMapper.toUserDto(userRepository.save(updateUserFromData(
                UserMapper.toUser(userId, userDto), user)));
    }

    private User findAndValidationUserInRepositiry(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new DataNotFound(
                String.format("User with id %d was not found in the database", userId)));
    }

    @Override
    public UserDto createUser(@Valid UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    private User updateUserFromData(User user, User userFromData) {
        if (user.getName() != null) {
            userFromData.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userFromData.setEmail(user.getEmail());
        }
        return userFromData;
    }
}
