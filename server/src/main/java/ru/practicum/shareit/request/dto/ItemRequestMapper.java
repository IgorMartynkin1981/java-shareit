package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        return new ItemRequest(userId, itemRequestDto.getDescription(), LocalDateTime.now());
    }

    public static InfoItemRequestDto toInfoItemRequestDto(ItemRequest itemRequest) {
        return new InfoItemRequestDto(itemRequest.getRequestId(),
                itemRequest.getDescription(),
                itemRequest.getCreationTime(),
                itemRequest.getItems());
    }
}
