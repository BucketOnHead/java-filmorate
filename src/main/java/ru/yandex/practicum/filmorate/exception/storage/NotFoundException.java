package ru.yandex.practicum.filmorate.exception.storage;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
