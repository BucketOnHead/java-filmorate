package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.storage.StorageException;

public class UserStorageException extends StorageException {
    public static final String USER_ALREADY_EXISTS = "The userID%d has already been added earlier.";
    public static final String USER_NOT_FOUND = "The userID%d was not found.";
    public static final String UNABLE_TO_ADD_YOURSELF = "The userID%d cannot add himself as a friend";
    public static final String UNABLE_TO_DELETE_YOURSELF = "The userID%d cannot delete himself as a friend";

    public UserStorageException(String message) {
        super(message);
    }
}
