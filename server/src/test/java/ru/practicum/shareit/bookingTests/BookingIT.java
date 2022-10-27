package ru.practicum.shareit.bookingTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.services.BookingServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.services.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.services.UserServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringJUnitConfig({ShareItServer.class, ItemServiceImpl.class, UserServiceImpl.class, BookingServiceImpl.class})
public class BookingIT {

    private final EntityManager em;
    private final ItemServiceImpl itemService;
    private final UserServiceImpl userService;
    private final BookingServiceImpl bookingService;

    @Test
    void createBooking() {
        UserDto userDto1 = ObjectsForTests.getUserDto1();
        userService.createUser(userDto1);
        UserDto userDto2 = ObjectsForTests.getUserDto2();
        userService.createUser(userDto2);
        ItemDto itemDto = ObjectsForTests.getItemDto3();
        itemDto.setRequestId(null);
        itemService.createItem(itemDto, 1L);
        itemService.createItem(itemDto, 1L);
        itemService.createItem(itemDto, 2L);
        BookingDto bookingDto = ObjectsForTests.futureBookingDto1();
        bookingService.createBooking(bookingDto, 1L);
        Booking booking = ObjectsForTests.futureBooking();

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking queryBooking = query
                .setParameter("id", 1L)
                .getSingleResult();
        Assertions.assertEquals(booking.getItem(), queryBooking.getItem());
    }
}
