package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.StorageException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MemoryItemDAOImpl implements ItemDAO {

    private final Map<Long, Item> items = new HashMap<>();
    private Long counter = 0L;

    @Override
    public Item addItem(Long ownerId, Item item) {
        item.setId(++counter);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item amendItem(Long itemId, Item item) {
        if (items.containsKey(itemId)) {
            items.put(itemId, item);
            return item;
        } else {
            throw new StorageException(
                    String.format("There are no any items with ID %d!", itemId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Item getItem(Long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new StorageException(
                    String.format("There are no any items with ID %d!", itemId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<Item> getAllItemsByOwner(Long ownerId) {
        return items.values()
                .stream()
                .filter(i -> i.getOwnerId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> search(String searchQuery) {
        if (searchQuery.isEmpty()) return new ArrayList<>();

        return items.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        i.getDescription().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
    }
}
