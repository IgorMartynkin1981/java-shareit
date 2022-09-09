package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String name;
    @NotNull
    @NotBlank(message = "Адрес электронной почты не может быть пустым.")
    @Email(message = "Email должен быть корректным адресом электронной почты")
    private String email;
}
