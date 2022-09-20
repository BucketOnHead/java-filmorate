package ru.yandex.practicum.filmorate.storage.film.exception;

public class LikeNotFoundException extends FilmStorageException {
    public static final String LIKE_NOT_FOUND = "Лайк пользователя userID_%d для фильма filmID%d не найден";

    public LikeNotFoundException(String message) {
        super(message);
    }
}
