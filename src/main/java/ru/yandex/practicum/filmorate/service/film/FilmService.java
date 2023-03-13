package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Service;

import java.util.Collection;

public interface FilmService extends Service<Film> {

    /**
     * Метод возвращает коллекцию состоящую из N
     * фильмов по популярности, основываясь на
     * количестве лайков у каждого фильма.
     *
     * @param count размер коллекции.
     * @return коллекция популярных фильмов.
     */
    Collection<Film> getPopularFilms(int count);

    /**
     * Метод добавляет пользовательский лайк фильму.
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     */
    void addLike(long filmID, long userID);

    /**
     * Метод удаляет пользовательский лайк у фильма.
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     */
    void deleteLike(long filmID, long userID);
}
