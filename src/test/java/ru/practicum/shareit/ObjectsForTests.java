package ru.practicum.shareit;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.InfoBookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.InfoCommentDto;
import ru.practicum.shareit.item.dto.InfoItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectsForTests {

    public static User getUser1() {
        return new User(1L, "user1", "user1@mail.ru");
    }

    public static UserDto getUserDto1() {
        return new UserDto(1L, "user1", "user1@mail.ru");
    }

    public static User getUser2() {
        return new User(2L, "user2", "user2@mail.ru");
    }

    public static UserDto getUserDto2() {
        return new UserDto(2L, "user2", "user2@mail.ru");
    }

    public static UserDto getUserDtoError() {
        return new UserDto(777L, "user2", "user2@mail.ru");
    }

    public static Item getItem1() {
        return new Item(1L, getUser1(), "кувалда", "кувалда с деревянной ручкой",
                true, new ArrayList<>());
    }

    public static ItemDto getItemDto1() {
        return new ItemDto(1L, "кувалда", "кувалда с деревянной ручкой",
                true);
    }

    public static ItemDto getItemDto3() {
        return new ItemDto(3L, "молоток", "молоток с деревянной ручкой",
                true);
    }

    public static InfoItemDto getInfoItemDto1() {
        InfoItemDto infoItemDto = new InfoItemDto(1L, getUser1(), "кувалда",
                "кувалда с деревянной ручкой",
                true, new ArrayList<>());
        infoItemDto.setRequestId(1L);
        return infoItemDto;
    }

    public static InfoItemDto itemDtoToOwner() {
        InfoItemDto infoItemDto = new InfoItemDto(1L, getUser1(), "кувалда",
                "кувалда с деревянной ручкой",
                true, new ArrayList<>());
        infoItemDto.setRequestId(1L);
        infoItemDto.setLastBooking(new InfoItemDto.BookingDto());
        infoItemDto.setNextBooking(new InfoItemDto.BookingDto());
        return infoItemDto;
    }

    public static Item getItem2() {
        return new Item(2L, getUser2(), "кувалда", "кувалда с металлической ручкой",
                true, new ArrayList<>());
    }

    public static Item getItem3() {
        return new Item(3L, getUser2(), "молоток", "молоток с деревянной ручкой",
                true, new ArrayList<>());
    }

    public static Booking futureBooking() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);
        return new Booking(1L, getUser1(), getItem3(), start, end, State.WAITING);
    }

    public static Booking pastBooking() {
        LocalDateTime start = LocalDateTime.of(2021, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2021, 10, 2, 12, 0);
        return new Booking(2L, getUser1(), getItem3(), start, end, State.APPROVED);
    }

    public static Booking rejectedBooking() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);
        return new Booking(1L, getUser1(), getItem3(), start, end, State.REJECTED);
    }

    public static CommentDto commentDto() {
        LocalDateTime time = LocalDateTime.of(2022, 9, 1, 12, 0);
        return new CommentDto(1L, "comment", 3L, 1L, time);
    }

    public static Comment comment() {
        LocalDateTime time = LocalDateTime.of(2022, 9, 1, 12, 0);
        return new Comment(1L, "comment", 3L, getUser1(), time);
    }

    public static InfoCommentDto infoCommentDto() {
        LocalDateTime time = LocalDateTime.of(2022, 9, 1, 12, 0);
        return new InfoCommentDto(1L, "comment", 3L, "user1", time);
    }

    public static BookingDto futureBookingDto1() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);
        return new BookingDto(1L, 1L, 3L, start, end);
    }

    public static InfoBookingDto futureInfoBookingDto1() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);
        return new InfoBookingDto(1L, getUser1(), getItem3(), start, end, State.WAITING);
    }

    public static InfoBookingDto waitingFutureInfoBookingDto1() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);
        return new InfoBookingDto(1L, getUser1(), getItem3(), start, end, State.WAITING);
    }

    public static InfoBookingDto approvedFutureInfoBookingDto1() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);
        return new InfoBookingDto(1L, getUser1(), getItem3(), start, end, State.APPROVED);
    }

    public static InfoBookingDto rejectedFutureInfoBookingDto1() {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);
        return new InfoBookingDto(1L, getUser1(), getItem3(), start, end, State.REJECTED);
    }

    public static List<Booking> bookingsForSetStatus() {
        Booking waitingBooking = new Booking(1L, getUser1(), getItem3(),
                LocalDateTime.of(2023, 10, 1, 12, 0),
                LocalDateTime.of(2023, 10, 2, 12, 0), State.WAITING);
        Booking rejectedBooking = new Booking(2L, getUser1(), getItem3(),
                LocalDateTime.of(2021, 10, 1, 12, 0),
                LocalDateTime.of(2021, 10, 2, 12, 0), State.REJECTED);
        Booking futureBooking = new Booking(3L, getUser1(), getItem3(),
                LocalDateTime.of(2023, 10, 1, 12, 0),
                LocalDateTime.of(2023, 10, 2, 12, 0), State.APPROVED);
        Booking pastBooking = new Booking(4L, getUser1(), getItem3(),
                LocalDateTime.of(2021, 10, 1, 12, 0),
                LocalDateTime.of(2021, 10, 2, 12, 0), State.APPROVED);
        Booking currentBooking = new Booking(5L, getUser1(), getItem3(),
                LocalDateTime.of(2021, 10, 1, 12, 0),
                LocalDateTime.of(2025, 10, 2, 12, 0), State.APPROVED);
        return new ArrayList<>(Arrays.asList(waitingBooking,
                rejectedBooking,
                futureBooking,
                pastBooking,
                currentBooking));
    }

    public static InfoBookingDto waitingInfoBookingDTO() {
        return new InfoBookingDto(1L, getUser1(), getItem3(),
                LocalDateTime.of(2023, 10, 1, 12, 0),
                LocalDateTime.of(2023, 10, 2, 12, 0), State.WAITING);
    }

    public static InfoBookingDto rejectedInfoBookingDTO() {
        return new InfoBookingDto(2L, getUser1(), getItem3(),
                LocalDateTime.of(2021, 10, 1, 12, 0),
                LocalDateTime.of(2021, 10, 2, 12, 0), State.REJECTED);
    }

    public static InfoBookingDto futureInfoBookingDTO() {
        return new InfoBookingDto(3L, getUser1(), getItem3(),
                LocalDateTime.of(2023, 10, 1, 12, 0),
                LocalDateTime.of(2023, 10, 2, 12, 0), State.APPROVED);
    }

    public static InfoBookingDto pastInfoBookingDTO() {
        return new InfoBookingDto(4L, getUser1(), getItem3(),
                LocalDateTime.of(2021, 10, 1, 12, 0),
                LocalDateTime.of(2021, 10, 2, 12, 0), State.APPROVED);
    }

    public static InfoBookingDto currentInfoBookingDTO() {
        return new InfoBookingDto(5L, getUser1(), getItem3(),
                LocalDateTime.of(2021, 10, 1, 12, 0),
                LocalDateTime.of(2025, 10, 2, 12, 0), State.APPROVED);
    }
}
