package ru.yandex.practicum.filmorate.exception.service;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
