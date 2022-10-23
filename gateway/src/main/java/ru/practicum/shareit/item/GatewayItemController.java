package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.Create;
import ru.practicum.shareit.exception.NullDataException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@Validated
public class GatewayItemController {

    private final ItemClient itemClient;

    @Autowired
    public GatewayItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@Validated({Create.class}) @RequestBody GatewayItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.createItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId, @RequestBody @Valid GatewayItemDto itemDto,
                                             @RequestHeader(name = "X-Sharer-User-Id", required = false) Long ownerId) {
        itemDto.setId(itemId);
        return itemClient.updateItem(itemDto, ownerId);

    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.findItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                        Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10")
                                                        Integer size) {
        return itemClient.findAllItemsByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam String text,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                              Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10")
                                              Integer size) {
        return itemClient.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody GatewayCommentDto commentDto) {
        return itemClient.createComment(itemId, userId, commentDto);
    }

}
