package ru.yandex.practicum.filmorate.storage.user.exception;

public class UserNotFoundException extends UserStorageException {
    public static final String USER_NOT_FOUND = "Пользователь userID_%d не найден";

    public UserNotFoundException(String message) {
        super(message);
    }
}
