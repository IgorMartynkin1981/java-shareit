package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.Item;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class InfoItemRequestDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private Collection<Item> items;

    public InfoItemRequestDto(Long id, String description, LocalDateTime creationTime, Collection<Item> items) {
        this.id = id;
        this.description = description;
        this.created = creationTime;
        this.items = items;
    }

}
