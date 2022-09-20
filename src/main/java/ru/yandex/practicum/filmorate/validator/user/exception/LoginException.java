package ru.yandex.practicum.filmorate.validator.user.exception;

public class LoginException extends UserValidatorException {
    public static final String INCORRECT_LOGIN = "Логин не может быть пустым и содержать пробелы: %s";

    public LoginException(String message) {
        super(message);
    }
}
