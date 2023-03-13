package ru.yandex.practicum.filmorate.storage.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Set;

public interface FilmStorage extends Storage<Film> {

    /**
     * Метод добавляет в хранилище связь между
     * фильмом и жанром.
     *
     * @param filmID идентификатор фильма.
     * @param genres идентификатор жанра.
     */
    void addGenres(long filmID, Set<Genre> genres);

    /**
     * Метод обновляет связь в хранилище между
     * фильмом и пользователем.
     *
     * @param filmID идентификатор фильма.
     * @param genres идентификатор жанра.
     */
    void updateGenres(long filmID, Set<Genre> genres);

    /**
     * Метод возвращает все жанры фильма, по
     * его идентификатору.
     *
     * @param filmID идентификатор фильма.
     * @return Уникальная коллекция с жанрами,
     * принадлежащая фильму.
     */
    Set<Genre> getGenres(long filmID);
}
