package ru.yandex.practicum.filmorate.validator.film.exception;

public class DescriptionException extends FilmValidatorException {
    public static final String INCORRECT_DESCRIPTION = "Максимальная длина описания — 200 символов: %s";

    public DescriptionException(String message) {
        super(message);
    }
}
