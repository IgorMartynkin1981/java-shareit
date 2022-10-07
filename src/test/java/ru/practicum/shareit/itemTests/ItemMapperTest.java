package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repositories.BookingRepository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.InfoItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemMapperTest {

    UserRepository userRepository;
    BookingRepository bookingRepository;
    ItemMapper itemMapper;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        itemMapper = new ItemMapper(userRepository, bookingRepository);
    }

    @Test
    void toInfoItemDto() {
        Item item = ObjectsForTests.getItem3();
        List<Booking> bookingList = new ArrayList<>(Arrays.asList(ObjectsForTests.futureBooking(),
                ObjectsForTests.pastBooking()));
        InfoItemDto infoItemDto = new InfoItemDto(item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new ArrayList<>());
        infoItemDto.setLastBooking(InfoItemDto.toBookingDto(ObjectsForTests.pastBooking()));
        infoItemDto.setNextBooking(InfoItemDto.toBookingDto(ObjectsForTests.futureBooking()));
        when(bookingRepository.findByItemId(any())).thenReturn(bookingList);
        Assertions.assertEquals(itemMapper.toInfoItemDto(item), infoItemDto);
    }

    @Test
    void toInfoItemDtoNotOwner() {
        Item item = ObjectsForTests.getItem3();
        InfoItemDto infoItemDto = new InfoItemDto(item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new ArrayList<>());
        Assertions.assertEquals(itemMapper.toInfoItemDtoNotOwner(item), infoItemDto);
    }

    @Test
    void toItem() {
        ItemDto itemDto = ObjectsForTests.getItemDto1();
        itemDto.setId(null);
        Item item = ObjectsForTests.getItem1();
        item.setId(null);
        item.setComments(null);
        when(userRepository.findById(any())).thenReturn(Optional.of(ObjectsForTests.getUser1()));
        Assertions.assertEquals(itemMapper.toItem(itemDto, 1L), item);
    }
}