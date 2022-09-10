package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;

public class StorageException extends CommonException {

    public StorageException(String message, HttpStatus code) {
        super(message, code);
    }
}
