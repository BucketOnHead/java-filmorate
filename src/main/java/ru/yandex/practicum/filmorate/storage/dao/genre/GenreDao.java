package ru.yandex.practicum.filmorate.storage.dao.genre;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.Collection;
import java.util.Set;

public interface GenreDao {
    /**
     * Метод добавляет в хранилище связь между
     * фильмом и жанром.
     *
     * @param filmID идентификатор фильма.
     * @param genres идентификатор жанра.
     */
    void add(long filmID, Set<Genre> genres);

    /**
     * Метод обновляет связь в хранилище между
     * фильмом и пользователем.
     *
     * @param filmID идентификатор фильма.
     * @param genres идентификатор жанра.
     */
    void update(long filmID, Set<Genre> genres);

    /**
     * Меьтод возвращает жанр из хранилища
     * по его идентификатору.
     *
     * @param genreID идентификатор жанра.
     * @return жанр, принадлежащий
     * идентификатору.
     */
    Genre get(int genreID);

    /**
     * Метод возвращает все жанры из хранилища.
     *
     * @return коллекция из всех жанров в
     * хранилище.
     */
    Collection<Genre> getAll();

    /**
     * Метод возвращает все жанры фильма, по
     * его идентификатору.
     *
     * @param filmID идентификатор фильма.
     * @return Уникальная коллекция с жанрами,
     * принадлежащая фильму.
     */
    Set<Genre> getGenres(long filmID);

    /**
     * Метод проверяет содержится ли в хранилище
     * указанный жанр.
     *
     * @param genreID идентификатор жанра.
     * @return Логическое значение, true - если
     * элемент с указанным идентификатором содержится
     * в хранилище, и false - если нет.
     */
    boolean contains(int genreID);
}
