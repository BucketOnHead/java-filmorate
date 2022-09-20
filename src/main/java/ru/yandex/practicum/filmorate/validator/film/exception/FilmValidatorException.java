package ru.yandex.practicum.filmorate.validator.film.exception;

import ru.yandex.practicum.filmorate.validator.ValidatorException;

public class FilmValidatorException extends ValidatorException {
    public FilmValidatorException(String message) {
        super(message);
    }
}
