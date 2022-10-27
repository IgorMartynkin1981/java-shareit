package ru.practicum.shareit.itemTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.services.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({ShareItServer.class, ItemServiceImpl.class, UserServiceImpl.class})
public class ItemIT {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;

    @Test
    void createItem() {
        UserDto userDto1 = ObjectsForTests.getUserDto1();
        userService.createUser(userDto1);
        ItemDto itemDto = ObjectsForTests.getItemDto1();
        itemDto.setRequestId(null);
        Item item = ObjectsForTests.getItem1();
        itemService.createItem(itemDto, 1L);
        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item queryItem = query
                .setParameter("id", 1L)
                .getSingleResult();
        Assertions.assertEquals(item, queryItem);
    }

}
