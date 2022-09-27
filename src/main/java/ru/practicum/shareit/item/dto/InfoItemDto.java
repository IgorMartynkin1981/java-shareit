package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class InfoItemDto {
    Long id;
    private User owner;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Collection<InfoCommentDto> comments;

    public InfoItemDto(Long id, User owner, String name, String description,
                       Boolean available, Collection<InfoCommentDto> comments) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.available = available;
        this.comments = comments;
    }

    @Data
    public static class BookingDto {
        private Long id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;

        public BookingDto(Long id, Long bookerId, LocalDateTime start, LocalDateTime end) {
            this.id = id;
            this.bookerId = bookerId;
            this.start = start;
            this.end = end;
        }
    }

    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return new BookingDto(booking.getId(), booking.getBooker().getId(), booking.getStart(), booking.getEnd());
    }
}
