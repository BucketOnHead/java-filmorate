package ru.yandex.practicum.filmorate.exception.storage;

public class ElementNotFoundException extends StorageException {
    public ElementNotFoundException(String message) {
        super(message);
    }
}
