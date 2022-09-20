package ru.yandex.practicum.filmorate.validator.film.exception;

public class DurationException extends FilmValidatorException {
    public static final String INCORRECT_DURATION = "Продолжительность фильма не может быть отрицательной %s";

    public DurationException(String message) {
        super(message);
    }
}
