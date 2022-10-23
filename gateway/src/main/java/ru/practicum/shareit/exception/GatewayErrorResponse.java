package ru.practicum.shareit.exception;

public class GatewayErrorResponse {

    private final String error;

    public GatewayErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
