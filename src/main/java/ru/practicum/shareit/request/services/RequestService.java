package ru.practicum.shareit.request.services;

import ru.practicum.shareit.request.dto.InfoItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface RequestService {
    InfoItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId);

    Collection<InfoItemRequestDto> findRequestsByUserId(Long userId);

    Collection<InfoItemRequestDto> findAllRequests(Long userId, Integer from, Integer size);

    InfoItemRequestDto findRequestById(Long requestId, Long userId);
}
