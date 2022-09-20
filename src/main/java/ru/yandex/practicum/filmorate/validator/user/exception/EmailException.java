package ru.yandex.practicum.filmorate.validator.user.exception;

public class EmailException extends UserValidatorException {
    public static final String INCORRECT_EMAIL =
            "Электронная почта не может быть пустой и должна содержать символ @: %s";

    public EmailException(String message) {
        super(message);
    }
}
