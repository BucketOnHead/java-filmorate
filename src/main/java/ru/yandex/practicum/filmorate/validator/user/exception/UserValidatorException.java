package ru.yandex.practicum.filmorate.validator.user.exception;

import ru.yandex.practicum.filmorate.validator.ValidatorException;

public class UserValidatorException extends ValidatorException {
    public UserValidatorException(String message) {
        super(message);
    }
}
