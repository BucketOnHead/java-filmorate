package ru.yandex.practicum.filmorate.controller.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    String error;
    String description;

    public ExceptionResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}