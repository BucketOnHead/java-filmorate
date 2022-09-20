package ru.yandex.practicum.filmorate.validator.film.exception;

public class DateException extends FilmValidatorException {
    public static final String INCORRECT_RELEASE_DATE = "Дата релиза не может быть раньше, чем 28-12-1895: %s";

    public DateException(String message) {
        super(message);
    }
}
