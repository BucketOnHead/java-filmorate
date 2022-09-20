package ru.yandex.practicum.filmorate.validator.film.exception;

public class NameException extends FilmValidatorException {
    public static final String INCORRECT_NAME = "Имя не может быть пустым: %s";

    public NameException(String message) {
        super(message);
    }
}
