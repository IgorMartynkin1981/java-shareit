package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "booking_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @Column(name = "booking_start", nullable = false)
    private LocalDateTime start;
    @Column(name = "booking_end", nullable = false)
    private LocalDateTime end;
    @Column(nullable = false)
    private State state;

    public Booking(User booker, Item item, LocalDateTime start, LocalDateTime end) {
        this.booker = booker;
        this.item = item;
        this.start = start;
        this.end = end;
    }
}
