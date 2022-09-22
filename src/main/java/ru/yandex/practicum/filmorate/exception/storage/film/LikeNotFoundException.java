package ru.yandex.practicum.filmorate.exception.storage.film;

import ru.yandex.practicum.filmorate.exception.storage.ElementNotFoundException;

public class LikeNotFoundException extends ElementNotFoundException {
    public static final String LIKE_NOT_FOUND = "Лайк пользователя userID_%d для фильма filmID_%d не найден";

    public LikeNotFoundException(String message) {
        super(message);
    }
}
