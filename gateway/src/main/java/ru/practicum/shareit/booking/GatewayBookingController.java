package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class GatewayBookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> findBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                               Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10")
                                               Integer size) {
        return bookingClient.findBookings(userId, stateParam, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findBookingsByOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(name = "state", defaultValue = "all")
                                                      String stateParam,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                      Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "10")
                                                      Integer size) {
        return bookingClient.findBookingsByOwner(userId, stateParam, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable Long bookingId) {
        return bookingClient.findBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId, @RequestParam Boolean approved,
                                                 @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return bookingClient.approveBooking(bookingId, approved, ownerId);
    }
}
