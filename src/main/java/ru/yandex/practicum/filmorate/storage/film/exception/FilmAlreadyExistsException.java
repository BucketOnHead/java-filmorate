package ru.yandex.practicum.filmorate.storage.film.exception;

public class FilmAlreadyExistsException extends FilmStorageException {
    public static final String FILM_ALREADY_EXISTS = "Фильм filmID_%d уже был добавлен ранее";

    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}
