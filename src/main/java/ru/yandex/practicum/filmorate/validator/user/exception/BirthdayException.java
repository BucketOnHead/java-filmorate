package ru.yandex.practicum.filmorate.validator.user.exception;

public class BirthdayException extends UserValidatorException {
    public static final String INCORRECT_BIRTHDAY = "Дата рождения не может быть в будущем: %s";

    public BirthdayException(String message) {
        super(message);
    }
}
