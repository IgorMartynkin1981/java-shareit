package ru.practicum.shareit.booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.InfoBookingDto;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.repositories.BookingRepository;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.exception.ErrorArgumentException;
import ru.practicum.shareit.exception.ValidationDataException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper mapper,
                              ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.mapper = mapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public InfoBookingDto createBooking(BookingDto bookingDto, Long bookerId) {
        Item item = findAndVerifyBooking(bookingDto, bookerId);
        User booker = findAndVerifyUser(bookerId);
        Booking booking = mapper.toBooking(bookingDto, item, booker);
        booking.setState(State.WAITING);
        return BookingMapper.toInfoBookingDto(bookingRepository.save(booking));
    }

    public InfoBookingDto approveBooking(Long bookingId, Boolean approved, Long ownerId) {
        Booking booking = findAndVerifyBookingInRepository(bookingId);
        verifyApprovedBooking(booking);
        verifyOwnerItem(ownerId, booking);
        if (approved) {
            booking.setState(State.APPROVED);
        } else {
            booking.setState(State.REJECTED);
        }
        return BookingMapper.toInfoBookingDto(bookingRepository.save(booking));
    }

    public InfoBookingDto findBookingById(Long bookingId, Long userId) {
        findAndVerifyUser(userId);
        Booking booking = findAndVerifyBookingInRepository(bookingId);
        verifyRequestOnOwnerItemOrUserCreated(userId, booking);
        return BookingMapper.toInfoBookingDto(booking);
    }

    public Collection<InfoBookingDto> findAllBookingsByUserId(Long userId, String state, Integer from, Integer size) {
        PageRequest pageRequest = createAndVerifyArgumentsForSetBookingsStatus(userId, state, from, size);
        return setBookingStatus(bookingRepository.findBookingsByBookerId(userId, pageRequest), state).stream()
                .map(BookingMapper::toInfoBookingDto).collect(Collectors.toList());
    }

    public Collection<InfoBookingDto> findAllBookingsByOwnerId(Long userId, String state, Integer from, Integer size) {
        PageRequest pageRequest = createAndVerifyArgumentsForSetBookingsStatus(userId, state, from, size);
        return setBookingStatus(bookingRepository.findBookingsByItemOwnerId(userId, pageRequest), state).stream()
                .map(BookingMapper::toInfoBookingDto).collect(Collectors.toList());
    }

    private PageRequest createAndVerifyArgumentsForSetBookingsStatus(Long userId, String state, Integer from,
                                                                     Integer size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("start").descending());
        findAndVerifyUser(userId);
        verifyArgumentState(state);
        return pageRequest;
    }

    private Collection<Booking> setBookingStatus(Collection<Booking> bookings, String state) {
        if (State.valueOf(state.toUpperCase()).equals(State.WAITING)) {
            return bookings.stream().filter((b) -> b.getState().equals(State.WAITING))
                    .collect(Collectors.toList());
        } else if (State.valueOf(state.toUpperCase()).equals(State.REJECTED)) {
            return bookings.stream().filter((b) -> b.getState().equals(State.REJECTED))
                    .collect(Collectors.toList());
        } else if (State.valueOf(state.toUpperCase()).equals(State.FUTURE)) {
            return bookings.stream().filter((b) -> LocalDateTime.now().isBefore(b.getStart()))
                    .collect(Collectors.toList());
        } else if (State.valueOf(state.toUpperCase()).equals(State.PAST)) {
            return bookings.stream().filter((b) -> LocalDateTime.now().isAfter(b.getEnd()))
                    .collect(Collectors.toList());
        } else if (State.valueOf(state.toUpperCase()).equals(State.CURRENT)) {
            return bookings.stream().filter((b) -> LocalDateTime.now().isAfter(b.getStart())
                    && LocalDateTime.now().isBefore(b.getEnd())).collect(Collectors.toList());
        }
        return bookings;
    }

    private Booking findAndVerifyBookingInRepository(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFound(
                String.format("Booking with id %d was not found in the database", bookingId)));
    }

    private static void verifyOwnerItem(Long ownerId, Booking booking) {
        if (!ownerId.equals(booking.getItem().getOwner().getId())) {
            throw new ValidationDataException("Only the owner of the item can confirm the request");
        }
    }

    private static void verifyApprovedBooking(Booking booking) {
        if (booking.getState().equals(State.APPROVED)) {
            throw new ErrorArgumentException("It is not possible to change the status of a confirmed booking");
        }
    }

    private static void verifyRequestOnOwnerItemOrUserCreated(Long userId, Booking booking) {
        if (!userId.equals(booking.getItem().getOwner().getId()) && !userId.equals(booking.getBooker().getId())) {
            throw new ValidationDataException("Booking data can only be requested by the owner of the item, " +
                    "or by the user who created the booking");
        }
    }

    private static void verifyArgumentState(String state) {
        try {
            State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ErrorArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private Item findAndVerifyBooking(BookingDto bookingDto, Long bookerId) {
        Item item = findAndVerifyItemInRepository(bookingDto);
        verifyAvailableItem(item);
        findAndVerifyUser(bookerId);
        verifyOwnerItem(bookerId, item);
        return item;
    }

    private static void verifyOwnerItem(Long bookerId, Item item) {
        if (item.getOwner().getId().equals(bookerId)) {
            throw new ValidationDataException("The owner cannot booking his thing");
        }
    }

    private Item findAndVerifyItemInRepository(BookingDto bookingDto) {
        return itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new DataNotFound(
                String.format("The requested item with id %d was not found in the database", bookingDto.getItemId())));
    }

    private static void verifyAvailableItem(Item item) {
        if (!item.getAvailable()) {
            throw new ErrorArgumentException("Booking of this item is not possible, item status is 'occupied'");
        }
    }

    private User findAndVerifyUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new DataNotFound(
                String.format("User with id %d was not found in the database", userId)));
    }
}
