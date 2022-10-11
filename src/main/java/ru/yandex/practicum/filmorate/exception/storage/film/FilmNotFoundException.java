package ru.yandex.practicum.filmorate.exception.storage.film;

import ru.yandex.practicum.filmorate.exception.storage.NotFoundException;

public class FilmNotFoundException extends NotFoundException {
    public static final String FILM_NOT_FOUND = "Фильм ID_%d не найден";

    public FilmNotFoundException(String message) {
        super(message);
    }
}
