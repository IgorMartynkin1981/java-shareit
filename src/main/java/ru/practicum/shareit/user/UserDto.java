package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class UserDto {
    @Positive
    private Long id;
    private String name;
    @NotNull
    @NotBlank(message = "Адрес электронной почты не может быть пустым.")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
}
