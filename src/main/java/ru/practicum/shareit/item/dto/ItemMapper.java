package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repositories.BookingRepository;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemMapper(UserRepository userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public InfoItemDto toInfoItemDto(Item item) {
        findAllComments(item);
        Collection<InfoCommentDto> commentsList = item.getComments().stream().map(CommentMapper::toInfoCommentDto)
                .collect(Collectors.toList());
        InfoItemDto infoItemDto = new InfoItemDto(item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                commentsList);
        Collection<Booking> bookingList = bookingRepository.findByItemId(infoItemDto.getId());
        infoItemDto.setLastBooking(InfoItemDto.toBookingDto(findLastBooking(bookingList)));
        infoItemDto.setNextBooking(InfoItemDto.toBookingDto(findNextBooking(bookingList)));
        return infoItemDto;
    }

    public InfoItemDto toInfoItemDtoNotOwner(Item item) {
        findAllComments(item);
        Collection<InfoCommentDto> commentsList = item.getComments().stream().map(CommentMapper::toInfoCommentDto)
                .collect(Collectors.toList());
        InfoItemDto infoItemDto = new InfoItemDto(item.getId(),
                item.getOwner(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                commentsList);
        infoItemDto.setLastBooking(null);
        infoItemDto.setNextBooking(null);
        return infoItemDto;
    }

    private static void findAllComments(Item item) {
        if (item.getComments() == null) {
            item.setComments(new ArrayList<Comment>());
        }
    }

    public Item toItem(ItemDto itemDto, Long ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new DataNotFound(
                String.format("User with id %d was not found in the database", ownerId)));
        return new Item(user, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    private Booking findLastBooking(Collection<Booking> bookingList) {
        return bookingList.stream()
                .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEnd)).orElse(null);
    }

    private Booking findNextBooking(Collection<Booking> bookingList) {
        return bookingList.stream()
                .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart)).orElse(null);
    }
}
