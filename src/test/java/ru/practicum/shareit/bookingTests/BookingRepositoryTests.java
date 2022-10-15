package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.repositories.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingRepositoryTests {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingRepositoryTests(UserRepository userRepository,
                                  ItemRepository itemRepository,
                                  BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
    }

    private User user1;
    private User user2;
    private Item item3;
    private Booking booking1;
    private Booking booking2;
    private static final LocalDateTime START = LocalDateTime.of(2023, 10, 1, 12, 0);
    private static final LocalDateTime END = LocalDateTime.of(2023, 10, 2, 12, 0);

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(new User(1L, "user1", "user1@mail.ru"));
        user2 = userRepository.save(new User(2L, "user2", "user2@mail.ru"));
        Item item1 = itemRepository.save(new Item(1L, user1, "кувалда", "кувалда с деревянной ручкой",
                true, new ArrayList<>()));
        Item item2 = itemRepository.save(new Item(2L, user2, "кувалда", "кувалда с металлической ручкой",
                true, new ArrayList<>()));
        item3 = itemRepository.save(new Item(3L, user2, "молоток", "молоток с деревянной ручкой",
                true, new ArrayList<>()));
        booking1 = bookingRepository.save(new Booking(1L, user1, item3, START, END, State.WAITING));
        booking2 = bookingRepository.save(new Booking(2L, user1, item2, START, END, State.WAITING));
    }

    @Test
    void findByItemIdTest() {
        Assertions.assertEquals(bookingRepository.findByItemId(item3.getId()), new ArrayList<>(List.of(booking1)));
    }

    @Test
    void findBookingsByItemOwnerIdTest() {
        List<Booking> listFromServer = bookingRepository.findBookingsByItemOwnerId(user2.getId(), Pageable.unpaged());
        Assertions.assertEquals(listFromServer, new ArrayList<>(List.of(booking1, booking2)));
    }

    @Test
    void findBookingsByBookerIdTest() {
        Assertions.assertEquals(bookingRepository.findBookingsByBookerId(user1.getId(), Pageable.unpaged()),
                new ArrayList<>(List.of(booking1, booking2)));
    }

    @Test
    void findBookingsByBookerIdAndItemIdTest() {
        Assertions.assertEquals(bookingRepository.findBookingsByBookerIdAndItemId(
                        booking1.getItem().getId(), user1.getId()),
                new ArrayList<>(List.of(booking1)));
    }
}
