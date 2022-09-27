package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.exception.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class ItemDto {

    @Positive (groups = {Create.class})
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank (groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;

    public ItemDto(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
