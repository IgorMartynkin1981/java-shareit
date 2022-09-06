package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@Data
@Builder
public class UserDto {
    @Positive
    private Long id;
    private String name;
    @Email
    private String email;
}
