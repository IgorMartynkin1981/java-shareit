package ru.practicum.shareit.booking.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Collection<Booking> findByItemId(Long itemId);

    List<Booking> findBookingsByItemOwnerId(Long booker, Pageable pageRequest);

    List<Booking> findBookingsByBookerId(Long booker, Pageable pageRequest);

    @Modifying
    @Query("select b from Booking as b " +
            "where b.item.id = ?1 and b.booker.id = ?2 " +
            "order by b.start desc")
    List<Booking> findBookingsByBookerIdAndItemId(Long itemId, Long userId);
}
