package ru.practicum.shareit.userTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repositories.UserRepository;
import ru.practicum.shareit.user.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    UserServiceImpl userService;
    UserRepository userRepository;
    User user = ObjectsForTests.getUser1();
    UserDto userDto = ObjectsForTests.getUserDto1();
    UserDto userDtoError = ObjectsForTests.getUserDtoError();

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void createUserTest() {
        when(userRepository.save(UserMapper.toUser(userDto)))
                .thenReturn(user);
        Assertions.assertEquals(userService.createUser(userDto), userDto);
    }

    @Test
    void updateUserTest() {
        when(userRepository.save(UserMapper.toUser(userDto)))
                .thenReturn(user);
        Assertions.assertEquals(userDto, userService.createUser(userDto));
        when(userRepository.save(UserMapper.toUser(userDtoError)))
                .thenThrow(new DataNotFound(String.format("User with id %d was not found in the database", 777)));
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> userService.createUser(userDtoError));
        Assertions.assertEquals(exception.getMessage(), "User with id 777 was not found in the database");
    }

    @Test
    void deleteUserTest() {
        userService.deleteUserById(5L);
        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(anyLong());
    }

    @Test
    void findUserByIdTest() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Assertions.assertEquals(userDto, userService.findUserById(1L));
        when(userRepository.findById(777L))
                .thenThrow(new DataNotFound(String.format("User with id %d was not found in the database", 777)));
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> userService.findUserById(777L));
        Assertions.assertEquals(exception.getMessage(), "User with id 777 was not found in the database");
    }

    @Test
    void findAllUsersTest() {
        when(userRepository.findAll())
                .thenReturn(new ArrayList<>(Collections.singleton(user)));
        Assertions.assertEquals(userService.findAllUsers(), new ArrayList<>(Collections.singleton(userDto)));
    }
}