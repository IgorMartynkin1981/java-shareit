package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.StorageException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryUserDAOImpl implements UserDAO {
    private final Map<Long, User> users = new HashMap<>();
    private Long counter = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++counter);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new StorageException(
                    String.format("User with ID: %d was not found", userId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User update(Long userId, User user) {
        if (users.containsKey(userId)) {
            users.put(userId, user);
            return user;
        } else {
            throw new StorageException(
                    String.format("User with ID: %d was not found", userId), HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }
}
