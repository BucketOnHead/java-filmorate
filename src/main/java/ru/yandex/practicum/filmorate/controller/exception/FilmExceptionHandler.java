package ru.yandex.practicum.filmorate.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.storage.film.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.storage.film.exception.FilmStorageException;
import ru.yandex.practicum.filmorate.storage.film.exception.LikeNotFoundException;
import ru.yandex.practicum.filmorate.validator.film.exception.FilmValidatorException;

@RestControllerAdvice()
public class FilmExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse filmValidatorExceptionHandle(final FilmValidatorException ex) {
        return new ExceptionResponse("Ошибка валидации фильма", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse filmNotFoundExceptionHandle(final FilmNotFoundException ex) {
        return new ExceptionResponse("фильм не найден", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse LikeNotFoundExceptionHandle(final LikeNotFoundException ex) {
        return new ExceptionResponse("лайк не найден", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse filmStorageExceptionHandle(final FilmStorageException ex) {
        return new ExceptionResponse("Ошибка хранилища", ex.getMessage());
    }
}
