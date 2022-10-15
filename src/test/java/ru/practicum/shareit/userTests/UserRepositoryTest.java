package ru.practicum.shareit.userTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.services.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({ShareItApp.class, UserServiceImpl.class})
public class UserRepositoryTest {

    private final EntityManager em;
    private final UserServiceImpl userService;

    @Test
    void findUserByIdTest() {
        User user = ObjectsForTests.getUser1();
        UserDto userDto = ObjectsForTests.getUserDto1();
        userDto.setId(null);
        userService.createUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User queryUser = query
                .setParameter("id", 1L)
                .getSingleResult();
        Assertions.assertEquals(user, queryUser);
    }
}
