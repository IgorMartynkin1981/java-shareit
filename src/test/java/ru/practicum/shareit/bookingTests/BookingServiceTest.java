package ru.practicum.shareit.bookingTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.repositories.BookingRepository;
import ru.practicum.shareit.booking.services.BookingServiceImpl;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.exception.ErrorArgumentException;
import ru.practicum.shareit.exception.ValidationDataException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
class BookingServiceTest {

    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingMapper mapper;
    BookingServiceImpl bookingService;

    @BeforeEach
    void beforeEach() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        mapper = mock(BookingMapper.class);
        bookingService = new BookingServiceImpl(bookingRepository, mapper, itemRepository, userRepository);
    }

    @Test
    void createBooking() {
        BookingDto bookingDto = ObjectsForTests.futureBookingDto1();
        bookingDto.setItemId(777L);
        Item item = ObjectsForTests.getItem3();
        item.setAvailable(false);
        when(itemRepository.findById(any()))
                .thenAnswer(invocationOnMock -> {
                    Long itemId = invocationOnMock.getArgument(0, Long.class);
                    if (itemId == 777) {
                        throw new DataNotFound("Item with id 777 was not found in the database");
                    } else {
                        return Optional.of(item);
                    }
                });
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.createBooking(bookingDto, 1L));
        Assertions.assertEquals("Item with id 777 was not found in the database",
                exception.getMessage());

        bookingDto.setItemId(3L);
        ErrorArgumentException exception1 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.createBooking(bookingDto, 1L));
        Assertions.assertEquals("Booking of this item is not possible, item status is 'occupied'",
                exception1.getMessage());

        item.setAvailable(true);
        bookingDto.setStart(LocalDateTime.now().minus(Period.ofDays(1)));
        ErrorArgumentException exception2 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.createBooking(bookingDto, 1L));
        Assertions.assertEquals("Incorrect booking dates",
                exception2.getMessage());

        bookingDto.setStart(LocalDateTime.of(2023, 10, 1, 12, 0));
        bookingDto.setEnd(LocalDateTime.of(2023, 10, 1, 11, 0));
        ErrorArgumentException exception3 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.createBooking(bookingDto, 1L));
        Assertions.assertEquals("Incorrect booking dates",
                exception3.getMessage());

        bookingDto.setEnd(LocalDateTime.of(2023, 10, 2, 12, 0));
        DataNotFound exception4 = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.createBooking(bookingDto, 2L));
        Assertions.assertEquals("User with id 2 was not found in the database",
                exception4.getMessage());

        userValidation();
        DataNotFound exception5 = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findBookingById(1L, 777L));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception5.getMessage());

        User booker = ObjectsForTests.getUser1();
        Booking booking = ObjectsForTests.futureBooking();
        when(mapper.toBooking(bookingDto, item, booker))
                .thenReturn(booking);
        when(bookingRepository.save(booking))
                .thenReturn(booking);

        Assertions.assertEquals(bookingService.createBooking(bookingDto, 1L),
                ObjectsForTests.waitingFutureInfoBookingDto1());
    }

    @Test
    void approveBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.futureBooking()));
        Booking approvedBooking = ObjectsForTests.futureBooking();
        approvedBooking.setState(State.APPROVED);
        when(bookingRepository.save(any())).thenReturn(approvedBooking);
        Assertions.assertEquals(ObjectsForTests.approvedFutureInfoBookingDto1(),
                bookingService.approveBooking(1L, true, 2L));

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.futureBooking()));
        Booking rejectedBooking = ObjectsForTests.futureBooking();
        rejectedBooking.setState(State.REJECTED);
        when(bookingRepository.save(any())).thenReturn(rejectedBooking);
        Assertions.assertEquals(ObjectsForTests.rejectedFutureInfoBookingDto1(),
                bookingService.approveBooking(1L, false, 2L));

        ValidationDataException exception = Assertions.assertThrows(
                ValidationDataException.class,
                () -> bookingService.approveBooking(1L, true, 777L));
        Assertions.assertEquals("Only the owner of the item can confirm the request",
                exception.getMessage());

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.pastBooking()));
        ErrorArgumentException exception1 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.approveBooking(1L, true, 2L));
        Assertions.assertEquals("It is not possible to change the status of a confirmed booking",
                exception1.getMessage());
    }

    @Test
    void getBookingById() {
        userValidation();
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findBookingById(1L, 777L));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception.getMessage());

        when(bookingRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Long bookingId = invocationOnMock.getArgument(0, Long.class);
                    if (bookingId == 777L) {
                        throw new DataNotFound(
                                String.format("Booking with id %d was not found in the database", 777));
                    } else {
                        return Optional.of(ObjectsForTests.futureBooking());
                    }
                });
        DataNotFound exception1 = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findBookingById(777L, 1L));
        Assertions.assertEquals("Booking with id 777 was not found in the database",
                exception1.getMessage());

        ValidationDataException exception2 = Assertions.assertThrows(
                ValidationDataException.class,
                () -> bookingService.findBookingById(1L, 333L));
        Assertions.assertEquals("Booking data can only be requested by the owner of the item, " +
                        "or by the user who created the booking",
                exception2.getMessage());

        Assertions.assertEquals(bookingService.findBookingById(1L, 1L),
                ObjectsForTests.futureInfoBookingDto1());
    }

    @Test
    void getBookingsByUserId() {
        userValidation();
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findAllBookingsByUserId(777L, "APPROVED", 0, 10));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception.getMessage());

        ErrorArgumentException exception1 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.findAllBookingsByUserId(1L, "None", 0, 10));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS",
                exception1.getMessage());

        when(bookingRepository.findBookingsByBookerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "WAITING", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.waitingInfoBookingDTO())));
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "REJECTED", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.rejectedInfoBookingDTO())));
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "FUTURE", 0, 10),
                new ArrayList<>(Arrays.asList(ObjectsForTests.waitingInfoBookingDTO()
                        , ObjectsForTests.futureInfoBookingDTO())));
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "CURRENT", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.currentInfoBookingDTO())));
    }

    @Test
    void getBookingsByOwnerId() {
        userValidation();
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findAllBookingsByOwnerId(777L, "APPROVED", 0, 10));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception.getMessage());

        ErrorArgumentException exception1 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.findAllBookingsByOwnerId(1L, "None", 0, 10));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS",
                exception1.getMessage());
        when(bookingRepository.findBookingsByItemOwnerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "WAITING", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.waitingInfoBookingDTO())));
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "REJECTED", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.rejectedInfoBookingDTO())));
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "FUTURE", 0, 10),
                new ArrayList<>(Arrays.asList(ObjectsForTests.waitingInfoBookingDTO()
                        , ObjectsForTests.futureInfoBookingDTO())));
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "CURRENT", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.currentInfoBookingDTO())));
    }

    void userValidation() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Long userId = invocationOnMock.getArgument(0, Long.class);
                    if (userId == 777L) {
                        throw new DataNotFound(
                                String.format("User with id %d was not found in the database", 777));
                    } else {
                        return Optional.of(ObjectsForTests.getUser1());
                    }
                });
    }
}