package ru.practicum.shareit.item;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(Long ownerId, ItemDto item);

    ItemDto amendItem(Long ownerId, Long itemId, ItemDto item);

    ItemDto getItem(Long itemId);

    Collection<ItemDto> getAllItemsByOwner(Long ownerId);

    Collection<ItemDto> search(String searchQuery);
}
