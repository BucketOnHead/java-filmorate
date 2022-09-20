package ru.yandex.practicum.filmorate.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.storage.user.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.storage.user.exception.UserStorageException;
import ru.yandex.practicum.filmorate.validator.user.exception.UserValidatorException;

@RestControllerAdvice()
public class UserExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse userValidatorExceptionHandle(final UserValidatorException ex) {
        return new ExceptionResponse("Ошибка валидации пользователя", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse userNotFoundExceptionHandle(final UserNotFoundException ex) {
        return new ExceptionResponse("Пользователь не найден", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse userStorageExceptionHandle(final UserStorageException ex) {
        return new ExceptionResponse("Ошибка хранилища", ex.getMessage());
    }
}

