package ru.yandex.practicum.filmorate.exception.storage.film;

import ru.yandex.practicum.filmorate.exception.storage.ElementAlreadyExists;

public class LikeAlreadyExistsException extends ElementAlreadyExists {
    public static final String LIKE_ALREADY_EXISTS = "Пользователь userID_%d уже ставил лайк фильму filmID_%d";

    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
