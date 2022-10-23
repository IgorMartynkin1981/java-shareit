package ru.practicum.shareit.item.services;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.InfoCommentDto;
import ru.practicum.shareit.item.dto.InfoItemDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    InfoItemDto createItem(ItemDto itemDto, Long ownerId);

    InfoItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId);

    InfoItemDto findItemById(Long itemId, Long userId);

    Collection<InfoItemDto> findAllItemsByOwnerId(Long ownerId, Integer from, Integer size);

    Collection<InfoItemDto> searchItemsByText(String text, Integer from, Integer size);

    InfoCommentDto createComment(Long itemId, Long userId, CommentDto commentDto);
}
