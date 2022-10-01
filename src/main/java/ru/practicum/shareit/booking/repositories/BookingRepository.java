package ru.practicum.shareit.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;

import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByItemId(Long itemId);

    @Modifying
    @Query("select b from Booking as b " +
            "where b.item.owner.id = ?1 " +
            "order by b.start desc")
    Collection<Booking> findAllBookingsByOwnerId(Long booker);

    @Modifying
    @Query("select b from Booking as b " +
            "where b.booker.id = ?1 " +
            "order by b.start desc")
    Collection<Booking> findAllBookingsByBooker(Long booker);

    @Modifying
    @Query("select b from Booking as b " +
            "where b.item.id = ?1 and b.booker.id = ?2 " +
            "order by b.start desc")
    Collection<Booking> findAllBookingsByBookerIdAndItemId(Long itemId, Long userId);
}
