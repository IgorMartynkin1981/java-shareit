package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
