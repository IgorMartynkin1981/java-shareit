package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DataNotFound;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repositories.UserRepository;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    private final UserRepository userRepository;

    @Autowired
    public CommentMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Comment toComment(Long itemId, Long userId, CommentDto commentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFound(
                String.format("User with id %d was not found in the database", userId)));

        return new Comment(commentDto.getText(), itemId, user, LocalDateTime.now());
    }

    public static InfoCommentDto toInfoCommentDto(Comment comment) {
        return new InfoCommentDto(comment.getId(),
                comment.getText(),
                comment.getItemId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
