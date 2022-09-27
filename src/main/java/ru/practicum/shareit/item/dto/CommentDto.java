package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    @Positive
    private Long id;
    @NotBlank
    private String text;
    private Long itemId;
    private Long authorId;
    private LocalDateTime created;

}
