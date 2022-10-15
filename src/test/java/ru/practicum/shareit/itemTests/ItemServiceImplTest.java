package ru.practicum.shareit.itemTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.ObjectsForTests;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repositories.BookingRepository;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.exception.ErrorArgumentException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repositories.CommentRepository;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.item.services.ItemServiceImpl;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ItemServiceImplTest {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemMapper mapper;
    private CommentMapper commentMapper;
    private ItemServiceImpl itemService;

    private User user1;
    private UserDto userError;
    private ItemDto itemDto1;
    private Item item1;
    private InfoItemDto infoItemDto1;
    private InfoItemDto infoItemDtoToOwner;
    private Booking futureBooking;
    private Booking pastBooking;
    private Booking rejectedBooking;
    private CommentDto commentDto;


    @BeforeEach
    void beforeEach() {
        itemRepository = mock(ItemRepository.class);
        userRepository = mock(UserRepository.class);
        bookingRepository = mock(BookingRepository.class);
        commentRepository = mock(CommentRepository.class);
        mapper = mock(ItemMapper.class);
        commentMapper = mock(CommentMapper.class);
        itemService = new ItemServiceImpl(itemRepository, mapper, userRepository, bookingRepository,
                commentRepository, commentMapper);

        user1 = ObjectsForTests.getUser1();
        userError = ObjectsForTests.getUserDtoError();
        itemDto1 = ObjectsForTests.getItemDto1();
        item1 = ObjectsForTests.getItem1();
        infoItemDto1 = ObjectsForTests.getInfoItemDto1();
        infoItemDtoToOwner = ObjectsForTests.itemDtoToOwner();
        futureBooking = ObjectsForTests.futureBooking();
        pastBooking = ObjectsForTests.pastBooking();
        rejectedBooking = ObjectsForTests.rejectedBooking();
        commentDto = ObjectsForTests.commentDto();
    }

    @Test
    void createItem() {
        userValidation();
        when(mapper.toItem(itemDto1, 1L)).thenReturn(item1);
        when(mapper.toInfoItemDto(item1)).thenReturn(infoItemDto1);
        when(itemRepository.save(item1)).thenReturn(item1);
        Assertions.assertEquals(infoItemDto1, itemService.createItem(itemDto1, user1.getId()));
    }

    @Test
    void updateItem() {
        userValidation();
        itemValidation();
        when(mapper.toInfoItemDto(item1)).thenReturn(infoItemDto1);
        when(itemRepository.save(item1)).thenReturn(item1);
        Assertions.assertEquals(itemService.updateItem(itemDto1.getId(), itemDto1, 1L), infoItemDto1);
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> itemService.updateItem(itemDto1.getId(), itemDto1, 777L));
        Assertions.assertEquals("User with id 777 was not found in the database", exception.getMessage());
    }

    @Test
    void getItemById() {
        userValidation();
        itemValidation();
        when(mapper.toInfoItemDto(any())).thenReturn(infoItemDto1);
        when(mapper.toInfoItemDtoNotOwner(any())).thenReturn(infoItemDtoToOwner);
        Assertions.assertEquals(itemService.findItemById(1L, 1L), infoItemDto1);
        Assertions.assertEquals(itemService.findItemById(1L, 333L), infoItemDtoToOwner);
    }

    @Test
    void getAllItemsByOwnerId() {
        userValidation();
        when(itemRepository.findByOwnerId(any(), any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(item1)));
        when(mapper.toInfoItemDto(any())).thenReturn(infoItemDto1);
        Assertions.assertEquals(new ArrayList<>(List.of(infoItemDto1)),
                itemService.findAllItemsByOwnerId(1L, 0, 10));
    }

    @Test
    void searchItems() {
        when(itemRepository.findByNameContainsOrDescriptionContainsIgnoreCase(any(), any(), any()))
                .thenReturn(new ArrayList<>(Collections.singletonList(item1)));
        when(mapper.toInfoItemDto(any())).thenReturn(infoItemDto1);
        Assertions.assertEquals(new ArrayList<>(List.of(infoItemDto1)),
                itemService.searchItemsByText("text", 0, 10));
    }

    @Test
    void createCommentErrorArgumentException() {
        userValidation();
        itemValidation();
        when(bookingRepository.findBookingsByBookerIdAndItemId(3L, 3L))
                .thenReturn(new ArrayList<>(Arrays.asList(futureBooking, rejectedBooking)));
        ErrorArgumentException exception = Assertions.assertThrows(
                ErrorArgumentException.class,
                () -> itemService.createComment(3L, 3L, commentDto));
        Assertions.assertEquals("Only the user who rented it can leave a comment on an item",
                exception.getMessage());
    }

    @Test
    void createComment() {
        userValidation();
        itemValidation();

        when(bookingRepository.findBookingsByBookerIdAndItemId(1L, 3L))
                .thenReturn(new ArrayList<>(Arrays.asList(futureBooking, pastBooking, rejectedBooking)));
        when(commentMapper.toComment(anyLong(), anyLong(), any()))
                .thenReturn(ObjectsForTests.comment());
        when(commentRepository.save(any()))
                .thenReturn(ObjectsForTests.comment());
        Assertions.assertEquals(itemService.createComment(1L, 3L, commentDto),
                ObjectsForTests.infoCommentDto());
    }

    @Test
    void userValidation() {
        when(userRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Long userId = invocationOnMock.getArgument(0, Long.class);
                    if (userId == 777L) {
                        throw new DataNotFound(
                                String.format("User with id %d was not found in the database", 777));
                    } else {
                        return Optional.of(user1);
                    }
                });
        DataNotFound exception = Assertions.assertThrows(
                DataNotFound.class,
                () -> itemService.createItem(itemDto1, userError.getId()));
        Assertions.assertEquals("User with id 777 was not found in the database",
                exception.getMessage());
    }

    void itemValidation() {
        when(itemRepository.findById(anyLong()))
                .thenAnswer(invocationOnMock -> {
                    Long id = invocationOnMock.getArgument(0, Long.class);
                    if (id == 777) {
                        throw new DataNotFound(
                                String.format("Item with id %d was not found in the database", 777));
                    } else {
                        return Optional.of(item1);
                    }
                });
    }
}