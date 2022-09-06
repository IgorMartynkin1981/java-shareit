package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Исключение валидации сущностей
 */
public class ValidationException extends CommonException {

    private BindingResult errors;

    public ValidationException(String message, HttpStatus code) {
        super(message, code);
    }

    public ValidationException(BindingResult errors, HttpStatus code) {
        super(null, code);
        this.errors = errors;
    }

    public List<String> getMessages() {
        if (errors != null) {
            return errors.getAllErrors()
                    .stream()
                    .map(ValidationException::getValidationMessage)
                    .collect(Collectors.toList());
        } else {
            return List.of(super.getMessage());
        }
    }

    @Override
    public String getMessage() {
        if (super.getMessage() == null) {
            return String.join(" ", this.getMessages());
        }
        return super.getMessage();
    }

    private static String getValidationMessage(ObjectError error) {
        if (error instanceof FieldError) {
            return error.getDefaultMessage();
        }
        return String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
    }
}