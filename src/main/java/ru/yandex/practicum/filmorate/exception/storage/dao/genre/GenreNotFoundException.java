package ru.yandex.practicum.filmorate.exception.storage.dao.genre;

import ru.yandex.practicum.filmorate.exception.storage.NotFoundException;

public class GenreNotFoundException extends NotFoundException {
    public static final String GENRE_NOT_FOUND = "Жанр ID_%d не найден";

    public GenreNotFoundException(String message) {
        super(message);
    }
}
