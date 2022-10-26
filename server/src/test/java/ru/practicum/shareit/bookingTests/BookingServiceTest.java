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

    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingServiceImpl bookingService;

    @BeforeEach
    void beforeEach() {
        bookingRepository = mock(BookingRepository.class);
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        BookingMapper mapper = mock(BookingMapper.class);
        bookingService = new BookingServiceImpl(bookingRepository, mapper, itemRepository, userRepository);
    }

    @Test
    void createBookingDataNotFound() {
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
    }

    @Test
    void createBookingErrorArgumentExceptionItemStatusIsOccupied() {
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

        bookingDto.setItemId(3L);
        ErrorArgumentException exception = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.createBooking(bookingDto, 1L));
        Assertions.assertEquals("Booking of this item is not possible, item status is 'occupied'",
                exception.getMessage());
    }

    @Test
    void approveBookingAPPROVED() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.futureBooking()));
        Booking approvedBooking = ObjectsForTests.futureBooking();
        approvedBooking.setState(State.APPROVED);
        when(bookingRepository.save(any())).thenReturn(approvedBooking);
        Assertions.assertEquals(ObjectsForTests.approvedFutureInfoBookingDto1(),
                bookingService.approveBooking(1L, true, 2L));
    }

    @Test
    void approveBookingREJECTED() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.futureBooking()));
        Booking rejectedBooking = ObjectsForTests.futureBooking();
        rejectedBooking.setState(State.REJECTED);
        when(bookingRepository.save(any())).thenReturn(rejectedBooking);
        Assertions.assertEquals(ObjectsForTests.rejectedFutureInfoBookingDto1(),
                bookingService.approveBooking(1L, false, 2L));
    }

    @Test
    void approveBookingValidationDataException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.futureBooking()));
        Booking rejectedBooking = ObjectsForTests.futureBooking();
        rejectedBooking.setState(State.REJECTED);
        when(bookingRepository.save(any())).thenReturn(rejectedBooking);
        ValidationDataException exception = Assertions.assertThrows(
                ValidationDataException.class,
                () -> bookingService.approveBooking(1L, true, 777L));
        Assertions.assertEquals("Only the owner of the item can confirm the request",
                exception.getMessage());
    }

    @Test
    void approveBookingValidationErrorArgumentException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.futureBooking()));
        Booking rejectedBooking = ObjectsForTests.futureBooking();
        rejectedBooking.setState(State.REJECTED);
        when(bookingRepository.save(any())).thenReturn(rejectedBooking);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(ObjectsForTests.pastBooking()));
        ErrorArgumentException exception = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.approveBooking(1L, true, 2L));
        Assertions.assertEquals("It is not possible to change the status of a confirmed booking",
                exception.getMessage());
    }

    @Test
    void findBookingByIdDataNotFoundUser() {
        userValidation();
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findBookingById(1L, 777L));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception.getMessage());
    }

    @Test
    void findBookingByIdDataNotFoundBooking() {
        userValidation();
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findBookingById(777L, 1L));
        Assertions.assertEquals("Booking with id 777 was not found in the database",
                exception.getMessage());
    }

    @Test
    void findByIdDataNotFoundBooking() {
        userValidation();
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
    }

    @Test
    void findAllBookingsByUserIdDataNotFound() {
        userValidation();
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findAllBookingsByUserId(777L, "APPROVED", 0, 10));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception.getMessage());
    }

    @Test
    void findAllBookingsByUserIdErrorArgumentException() {
        userValidation();
        ErrorArgumentException exception1 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.findAllBookingsByUserId(1L, "None", 0, 10));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS",
                exception1.getMessage());
    }

    @Test
    void findAllBookingsByUserIdSetStatusWAITING() {
        userValidation();
        when(bookingRepository.findBookingsByBookerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "WAITING", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.waitingInfoBookingDTO())));
    }

    @Test
    void findAllBookingsByUserIdSetStatusREJECTED() {
        userValidation();
        when(bookingRepository.findBookingsByBookerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "REJECTED", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.rejectedInfoBookingDTO())));
    }

    @Test
    void findAllBookingsByUserIdSetStatusFUTURE() {
        userValidation();
        when(bookingRepository.findBookingsByBookerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "FUTURE", 0, 10),
                new ArrayList<>(Arrays.asList(ObjectsForTests.waitingInfoBookingDTO(),
                        ObjectsForTests.futureInfoBookingDTO())));
    }

    @Test
    void findAllBookingsByUserIdSetStatusCURRENT() {
        userValidation();
        when(bookingRepository.findBookingsByBookerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByUserId(1L, "CURRENT", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.currentInfoBookingDTO())));
    }

    @Test
    void findAllBookingsByOwnerIdDataNotFound() {
        userValidation();
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> bookingService.findAllBookingsByOwnerId(777L, "APPROVED", 0, 10));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception.getMessage());
    }

    @Test
    void findAllBookingsByOwnerIdErrorArgumentException() {
        userValidation();
        ErrorArgumentException exception1 = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> bookingService.findAllBookingsByOwnerId(1L, "None", 0, 10));
        Assertions.assertEquals("Unknown state: UNSUPPORTED_STATUS",
                exception1.getMessage());
    }

    @Test
    void findBookingsByItemOwnerIdSetStatusWAITING() {
        userValidation();
        when(bookingRepository.findBookingsByItemOwnerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "WAITING", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.waitingInfoBookingDTO())));
    }

    @Test
    void findBookingsByItemOwnerIdSetStatusREJECTED() {
        userValidation();
        when(bookingRepository.findBookingsByItemOwnerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "REJECTED", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.rejectedInfoBookingDTO())));
    }

    @Test
    void findBookingsByItemOwnerIdSetStatusFUTURE() {
        userValidation();
        when(bookingRepository.findBookingsByItemOwnerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "FUTURE", 0, 10),
                new ArrayList<>(Arrays.asList(ObjectsForTests.waitingInfoBookingDTO(),
                        ObjectsForTests.futureInfoBookingDTO())));
    }

    @Test
    void findBookingsByItemOwnerIdSetStatusCURRENT() {
        userValidation();
        when(bookingRepository.findBookingsByItemOwnerId(anyLong(), any()))
                .thenReturn(ObjectsForTests.bookingsForSetStatus());
        Assertions.assertEquals(bookingService.findAllBookingsByOwnerId(1L, "CURRENT", 0, 10),
                new ArrayList<>(List.of(ObjectsForTests.currentInfoBookingDTO())));
    }

    private void userValidation() {
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