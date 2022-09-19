package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.storage.StorageException;

public class FilmStorageException extends StorageException {
    public static final String FILM_ALREADY_EXISTS = "The filmID%d has already been added earlier.";
    public static final String FILM_NOT_FOUND = "The filmID%d was not found.";
    public static final String FILM_ALREADY_EVALUATED = "the filmID%d has already been evaluated by userID%d";
    public static final String LIKE_NOT_FOUND = "the userID%d's like for the filmID%d was not found";

    public FilmStorageException(String message) {
        super(message);
    }
}
