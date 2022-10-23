package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class GatewayItemRequestController {

    private final RequestClient requestClient;

    @Autowired
    public GatewayItemRequestController(RequestClient requestClient) {
        this.requestClient = requestClient;
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid GatewayItemRequestDto itemRequestDto) {
        return requestClient.createRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findRequestsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.findRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                  Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10")
                                                  Integer size) {
        return requestClient.findAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @PathVariable @Positive Long requestId) {
        return requestClient.findRequestById(requestId, userId);
    }

}
