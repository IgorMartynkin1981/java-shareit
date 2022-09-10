package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;

public class AccessViolationException extends CommonException {

    public AccessViolationException(String message, HttpStatus code) {
        super(message, code);
    }
}
