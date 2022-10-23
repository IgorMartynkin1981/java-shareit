package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GatewayErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GatewayErrorResponse handleNullPointerException(final NullPointerException e) {
        return new GatewayErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GatewayErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new GatewayErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GatewayErrorResponse handleNullDataException(final NullDataException e) {
        StackTraceElement[] stack = e.getStackTrace();
        return new GatewayErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GatewayErrorResponse handleIllegalArgumentException(final IllegalArgumentException e) {
        return new GatewayErrorResponse(e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GatewayErrorResponse handleThrowable(final Throwable e) {
        StackTraceElement[] stack = e.getStackTrace();
        return new GatewayErrorResponse("An unexpected error has occurred.");
    }
}
