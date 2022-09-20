package ru.yandex.practicum.filmorate.storage.film.exception;

public class LikeAlreadyExistsException extends FilmStorageException {
    public static final String LIKE_ALREADY_EXISTS = "Пользователь userID_%d уже ставил лайк фильму filmID%d";

    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
