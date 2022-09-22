package ru.yandex.practicum.filmorate.exception.storage.user;

import ru.yandex.practicum.filmorate.exception.storage.ElementAlreadyExists;

public class UserAlreadyExistsException extends ElementAlreadyExists {
    public static final String USER_ALREADY_EXISTS = "Пользователь userID_%d уже был добавлен ранее";

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
