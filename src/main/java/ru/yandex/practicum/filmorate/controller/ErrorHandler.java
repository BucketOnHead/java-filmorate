package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.storage.NotFoundException;

import static ru.yandex.practicum.filmorate.service.Service.EXCEPTION_MESSAGE;

@Slf4j
@RestControllerAdvice()
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse methodArgumentNotValidExceptionHandle(final MethodArgumentNotValidException ex) {
        log.info(EXCEPTION_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());
        return new ExceptionResponse("Ошибка валидации", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse storageNotFoundExceptionHandle(final NotFoundException ex) {
        log.info(EXCEPTION_MESSAGE, ex.getClass().getSimpleName(), ex.getMessage());
        return new ExceptionResponse("Запрашиваемый ресурс не найден", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse throwableHandle(final Throwable th) {
        log.info(EXCEPTION_MESSAGE, th.getClass().getSimpleName(), th.getMessage());
        return new ExceptionResponse("Ошибка сервера", th.getMessage());
    }

    @Getter
    @AllArgsConstructor
    static class ExceptionResponse {
        String error;
        String description;
    }
}





