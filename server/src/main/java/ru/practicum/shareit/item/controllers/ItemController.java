package ru.practicum.shareit.item.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.InfoCommentDto;
import ru.practicum.shareit.item.dto.InfoItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public InfoItemDto createItem(@RequestBody ItemDto itemDto,
                                  @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public InfoItemDto updateItem(@PathVariable Long itemId,
                                  @RequestBody ItemDto itemDto,
                                  @RequestHeader(name = "X-Sharer-User-Id", required = false) Long ownerId) {
        return itemService.updateItem(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public InfoItemDto findItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping
    public Collection<InfoItemDto> findAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                         @RequestParam(name = "from", defaultValue = "0")
                                                         Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10")
                                                         Integer size) {
        return itemService.findAllItemsByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public Collection<InfoItemDto> searchItemsByText(@RequestParam String text,
                                                     @RequestParam(name = "from", defaultValue = "0")
                                                     Integer from,
                                                     @RequestParam(name = "size", defaultValue = "10")
                                                     Integer size) {
        return itemService.searchItemsByText(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public InfoCommentDto createComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
