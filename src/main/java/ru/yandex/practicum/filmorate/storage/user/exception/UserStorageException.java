package ru.yandex.practicum.filmorate.storage.user.exception;

import ru.yandex.practicum.filmorate.storage.StorageException;

public class UserStorageException extends StorageException {
    public UserStorageException(String message) {
        super(message);
    }
}
