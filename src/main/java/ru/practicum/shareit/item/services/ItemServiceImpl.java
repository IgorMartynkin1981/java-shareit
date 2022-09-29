package ru.practicum.shareit.item.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.repositories.BookingRepository;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.exception.ErrorArgumentException;
import ru.practicum.shareit.exception.NullDataException;
import ru.practicum.shareit.exception.ValidationDataException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repositories.CommentRepository;
import ru.practicum.shareit.item.repositories.ItemRepository;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ItemMapper mapper, UserRepository userRepository,
                           BookingRepository bookingRepository, CommentRepository commentRepository,
                           CommentMapper commentMapper) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public InfoItemDto createItem(ItemDto itemDto, Long ownerId) {
        findAndValidationUser(ownerId);
        return mapper.toInfoItemDto(itemRepository.save(mapper.toItem(itemDto, ownerId)));
    }

    public InfoItemDto updateItem(Long itemId, ItemDto itemDto, Long ownerId) {
        itemDto.setId(itemId);
        validationOnNullDataException(ownerId);
        Item item = findAndValidationItemInRepository(itemDto.getId());
        findAndValidationUser(ownerId);
        return mapper.toInfoItemDto(itemRepository.save(updateItemFromRepository(itemDto, ownerId, item)));
    }

    private static void validationOnNullDataException(Long ownerId) {
        if (ownerId == null) {
            throw new NullDataException("Item owner id missing in request");
        }
    }

    public InfoItemDto findItemById(Long itemId, Long userId) {
        findAndValidationUser(userId);
        Item item = findAndValidationItemInRepository(itemId);
        InfoItemDto infoItemDto;
        if (item.getOwner().getId().equals((userId))) {
            infoItemDto = mapper.toInfoItemDto(item);
        } else {
            infoItemDto = mapper.toInfoItemDtoNotOwner(item);
        }
        return infoItemDto;
    }

    public Collection<InfoItemDto> findAllItemsByOwnerId(Long ownerId) {
        findAndValidationUser(ownerId);
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(mapper::toInfoItemDto)
                .sorted(Comparator.comparing(InfoItemDto::getId))
                .collect(Collectors.toList());
    }

    public Collection<InfoItemDto> searchItemsByText(String text) {
        if (text.equals("")) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameContainsOrDescriptionContainsIgnoreCase(text, text)
                .stream().filter(Item::getAvailable)
                .map(mapper::toInfoItemDto)
                .collect(Collectors.toList());
    }

    public InfoCommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        findAndValidationItemInRepository(itemId);
        findAndValidationUser(userId);
        if (commentDto.getText() == null || commentDto.getText().equals("")) {
            throw new ErrorArgumentException("Comment cannot be empty");
        }
        Collection<Booking> bookingList = bookingRepository.findAllBookingsByBookerIdAndItemId(itemId, userId);
        bookingList.removeIf((b) -> b.getState().equals(State.REJECTED));
        bookingList.removeIf((b) -> b.getEnd().isAfter(LocalDateTime.now()));
        if (bookingList.size() == 0) {
            throw new ErrorArgumentException("Only the user who rented it can leave a comment on an item");
        }
        return CommentMapper.toInfoCommentDto(commentRepository
                .save(commentMapper.toComment(itemId, userId, commentDto)));
    }

    private Item updateItemFromRepository(ItemDto itemDto, Long ownerId, Item item) {
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ValidationDataException("Incorrect owner of item");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    private Item findAndValidationItemInRepository(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new DataNotFound(
                String.format("Items with id %d were not found in the database", itemId)));
    }

    private void findAndValidationUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new DataNotFound(
                String.format("User with id %d was not found in the database", userId)));
    }
}
