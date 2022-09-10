package ru.practicum.shareit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CommonException extends RuntimeException {

    private final String message;
    private final HttpStatus code;

    public CommonException(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }
}