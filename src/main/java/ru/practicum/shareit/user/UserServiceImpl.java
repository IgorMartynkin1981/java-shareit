package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User.UserBuilder;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Реализация сервиса пользователей
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userDAO.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userDAO.getUserById(userId));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userDAO.getUserById(userId);
        UserBuilder builder = user.toBuilder();

        if (userDto.getName() != null) {
            builder.name(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            if (emailIsValid(userDto.getEmail())) {
                builder.email(userDto.getEmail());
            } else {
                throw new ValidationException(String.format("User with email %s already exists!", user.getEmail()), HttpStatus.CONFLICT);
            }
        }

        return UserMapper.toUserDto(userDAO.update(userId, builder.build()));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        if (emailIsValid(user.getEmail())) {
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                return UserMapper.toUserDto(userDAO.createUser(user));

            } else {
                throw new ValidationException("User email cannot be empty or null", HttpStatus.BAD_REQUEST);
            }

        } else {
            throw new ValidationException(String.format("User with email %s already exists!", user.getEmail()), HttpStatus.CONFLICT);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userDAO.delete(userId);
    }


    private boolean emailIsValid(String email) {
        return userDAO.getAllUsers()
                .stream()
                .map(User::getEmail)
                .noneMatch(str -> str.equals(email));
    }
}
