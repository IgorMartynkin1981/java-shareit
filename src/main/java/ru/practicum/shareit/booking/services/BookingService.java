package ru.practicum.shareit.booking.services;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InfoBookingDto;

import java.util.Collection;

public interface BookingService {
    InfoBookingDto createBooking(BookingDto bookingDto, Long bookerId);

    InfoBookingDto approveBooking(Long bookingId, Boolean approved, Long ownerId);

    InfoBookingDto findBookingById(Long bookingId, Long userId);

    Collection<InfoBookingDto> findAllBookingsByUserId(Long userId, String state);

    Collection<InfoBookingDto> findAllBookingsByOwnerId(Long userId, String state);
}
