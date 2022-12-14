package ru.practicum.shareit.request.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.InfoItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.services.RequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private final RequestService requestService;

    @Autowired
    public ItemRequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public InfoItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        return requestService.createRequest(itemRequestDto, userId);

    }

    @GetMapping
    public Collection<InfoItemRequestDto> findRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.findRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public Collection<InfoItemRequestDto> findAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(name = "from", defaultValue = "0")
                                                          Integer from,
                                                          @RequestParam(name = "size", defaultValue = "10")
                                                          Integer size) {
        return requestService.findAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public InfoItemRequestDto findRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long requestId) {
        return requestService.findRequestById(requestId, userId);
    }
}
