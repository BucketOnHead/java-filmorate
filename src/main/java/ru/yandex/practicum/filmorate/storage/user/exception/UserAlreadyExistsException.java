package ru.yandex.practicum.filmorate.storage.user.exception;

public class UserAlreadyExistsException extends UserStorageException {
    public static final String USER_ALREADY_EXISTS = "Пользователь userID_%d уже был добавлен ранее";

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
