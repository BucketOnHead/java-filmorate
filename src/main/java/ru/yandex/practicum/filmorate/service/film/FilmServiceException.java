package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.service.ServiceException;

public class FilmServiceException extends ServiceException {
    public static final String FILM_ALREADY_EXISTS;
    public static final String FILM_NOT_FOUND;

    static {
        FILM_ALREADY_EXISTS = "Фильм уже добавлен ранее >%s";
        FILM_NOT_FOUND = "Фильм не найден >%s";
    }

    public FilmServiceException(String message) {
        super(message);
    }
}
