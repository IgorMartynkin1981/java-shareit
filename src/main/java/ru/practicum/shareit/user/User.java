package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String name;
    private String email;
}
