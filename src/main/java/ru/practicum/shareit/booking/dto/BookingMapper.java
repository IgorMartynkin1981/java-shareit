package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Component
public class BookingMapper {

    public Booking toBooking(BookingDto bookingDto, Item item, User booker) {
        return new Booking(booker, item, bookingDto.getStart(), bookingDto.getEnd());
    }

    public static InfoBookingDto toInfoBookingDto(Booking booking) {
        return new InfoBookingDto(booking.getId(),
                booking.getBooker(),
                booking.getItem(),
                booking.getStart(),
                booking.getEnd(),
                booking.getState());
    }
}
