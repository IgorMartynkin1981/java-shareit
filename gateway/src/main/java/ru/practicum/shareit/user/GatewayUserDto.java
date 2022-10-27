package ru.practicum.shareit.user;

import lombok.Data;
import ru.practicum.shareit.exception.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class GatewayUserDto {

    @Positive(groups = {Create.class})
    private Long id;
    @NotBlank (groups = {Create.class})
    private String name;
    @Email (groups = {Create.class}) @NotBlank (groups = {Create.class})
    private String email;

    public GatewayUserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public GatewayUserDto() {
    }
}

