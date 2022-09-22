package ru.yandex.practicum.filmorate.exception.storage.film;

import ru.yandex.practicum.filmorate.exception.storage.ElementNotFoundException;

public class FilmNotFoundException extends ElementNotFoundException {
    public static final String FILM_NOT_FOUND = "Фильм filmID_%d не найден";

    public FilmNotFoundException(String message) {
        super(message);
    }
}
