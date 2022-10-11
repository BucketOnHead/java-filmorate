package ru.yandex.practicum.filmorate.exception.storage.dao.mpa;

import ru.yandex.practicum.filmorate.exception.storage.NotFoundException;

public class MpaNotFoundException extends NotFoundException {
    public static final String MPA_NOT_FOUND = "Рейтинг MPA ID_%d не найден";

    public MpaNotFoundException(String message) {
        super(message);
    }
}
