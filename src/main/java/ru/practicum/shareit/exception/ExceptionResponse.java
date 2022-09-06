package ru.practicum.shareit.exception;

import lombok.Builder;
import lombok.Data;

/**
 * Модель для эдвайса исключений
 */
@Data
@Builder
public class ExceptionResponse {
    private String statusCode;
    private String timestamp;
    private String exceptionType;
    private String message;
}