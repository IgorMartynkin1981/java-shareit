package ru.practicum.shareit.itemTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@DataJpaTest
public class ItemRepositoryTests {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;
    private Item item3;

    @Autowired
    public ItemRepositoryTests(UserRepository userRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(1L, "user1", "user1@mail.ru"));
        user2 = userRepository.save(new User(2L, "user2", "user2@mail.ru"));
        item1 = itemRepository.save(new Item(1L, user1, "кувалда", "кувалда с деревянной ручкой",
                true, new ArrayList<>()));
        item2 = itemRepository.save(new Item(2L, user2, "кувалда", "кувалда с металлической ручкой",
                true, new ArrayList<>()));
        item3 = itemRepository.save(new Item(3L, user2, "молоток", "молоток с деревянной ручкой",
                true, new ArrayList<>()));
    }

    @Test
    void findByOwnerIdTest() {
        Page<Item> byOwner = itemRepository.findByOwnerId(user2.getId(), Pageable.unpaged());
        List<Item> list = byOwner.stream().collect(Collectors.toList());
        Assertions.assertEquals(list, new ArrayList<>(Arrays.asList(item2, item3)));
    }

    @Test
    void findByNameContainsOrDescriptionContainsIgnoreCaseTest() {
        Assertions.assertEquals(itemRepository.findByNameContainsOrDescriptionContainsIgnoreCase(
                        "кувалда", "кувалда", Pageable.unpaged()),
                new ArrayList<>(Arrays.asList(item1, item2)));
    }
}
