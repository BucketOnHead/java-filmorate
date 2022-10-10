package ru.yandex.practicum.filmorate.exception.storage.film;

public class LikeAlreadyExistsException extends RuntimeException {
    public static final String LIKE_ALREADY_EXISTS = "Лайк фильму ID_%d от пользователя ID_%d уже существует";

    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
