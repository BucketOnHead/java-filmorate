package ru.yandex.practicum.filmorate.exception.storage.user;

import ru.yandex.practicum.filmorate.exception.storage.ElementNotFoundException;

public class UserNotFoundException extends ElementNotFoundException {
    public static final String USER_NOT_FOUND = "Пользователь userID_%d не найден";

    public UserNotFoundException(String message) {
        super(message);
    }
}
