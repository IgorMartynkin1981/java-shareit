package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.State;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private Long bookerId;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private State state;

    public BookingDto(Long id, Long bookerId, Long itemId, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.bookerId = bookerId;
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
