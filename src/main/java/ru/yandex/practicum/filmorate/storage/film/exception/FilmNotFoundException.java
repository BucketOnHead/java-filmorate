package ru.yandex.practicum.filmorate.storage.film.exception;

public class FilmNotFoundException extends FilmStorageException {
    public static final String FILM_NOT_FOUND = "Фильм filmID_%d не найден";

    public FilmNotFoundException(String message) {
        super(message);
    }
}
