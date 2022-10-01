package ru.practicum.shareit.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InfoBookingDto;
import ru.practicum.shareit.booking.services.BookingService;
import ru.practicum.shareit.booking.services.BookingServiceImpl;
import ru.practicum.shareit.exception.Create;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public InfoBookingDto createBooking(@Validated({Create.class}) @RequestBody BookingDto bookingDto,
                                        @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public InfoBookingDto approveBooking(@PathVariable Long bookingId, @RequestParam Boolean approved,
                                         @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return bookingService.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public InfoBookingDto findBookingById(@PathVariable Long bookingId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findBookingById(bookingId, userId);
    }

    @GetMapping
    public Collection<InfoBookingDto> findAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                              @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public Collection<InfoBookingDto> findAllBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByOwnerId(userId, state);
    }
}
