package ru.yandex.practicum.filmorate.exception.storage;

public class ElementAlreadyExists extends StorageException {
    public ElementAlreadyExists(String message) {
        super(message);
    }
}
