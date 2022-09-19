package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.service.ServiceException;

public class UserServiceException extends ServiceException {
    public static final String USER_ALREADY_EXISTS;
    public static final String USER_NOT_FOUND;

    static {
        USER_ALREADY_EXISTS = "Пользователь уже добавлен ранее >%s";
        USER_NOT_FOUND = "Пользователь не найден >%s";
    }

    public UserServiceException(String message) {
        super(message);
    }
}
