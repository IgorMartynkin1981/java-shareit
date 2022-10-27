package ru.practicum.shareit.request.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.InfoItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repositories.RequestRepository;
import ru.practicum.shareit.user.services.UserService;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, UserService userService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
    }

    public InfoItemRequestDto createRequest(ItemRequestDto itemRequestDto, Long userId) {
        userService.findUserById(userId);
        ItemRequest itemRequest = requestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto, userId));
        return ItemRequestMapper.toInfoItemRequestDto(itemRequest);
    }

    public Collection<InfoItemRequestDto> findRequestsByUserId(Long userId) {
        userService.findUserById(userId);
        return requestRepository.findAllByUserId(userId)
                .stream()
                .map(ItemRequestMapper::toInfoItemRequestDto)
                .collect(Collectors.toList());
    }

    public Collection<InfoItemRequestDto> findAllRequests(Long userId, Integer from, Integer size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("creationTime").descending());
        userService.findUserById(userId);
        return requestRepository.findAllByUserIdNot(userId, pageRequest)
                .stream()
                .map(ItemRequestMapper::toInfoItemRequestDto)
                .collect(Collectors.toList());
    }

    public InfoItemRequestDto findRequestById(Long requestId, Long userId) {
        userService.findUserById(userId);
        ItemRequest itemRequest = findAndVerifyItemRequest(requestId);
        return ItemRequestMapper.toInfoItemRequestDto(itemRequest);
    }

    private ItemRequest findAndVerifyItemRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFound(
                        String.format("Request with id %d was not found in the database", requestId)));
    }
}
