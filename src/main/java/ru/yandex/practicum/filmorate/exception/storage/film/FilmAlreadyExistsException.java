package ru.yandex.practicum.filmorate.exception.storage.film;

import ru.yandex.practicum.filmorate.exception.storage.ElementAlreadyExists;

public class FilmAlreadyExistsException extends ElementAlreadyExists {
    public static final String FILM_ALREADY_EXISTS = "Фильм filmID_%d уже был добавлен ранее";

    public FilmAlreadyExistsException(String message) {
        super(message);
    }
}
