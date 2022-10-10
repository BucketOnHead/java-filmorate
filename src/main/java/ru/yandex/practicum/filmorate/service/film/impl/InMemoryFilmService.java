package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.storage.film.LikeAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.film.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException.FILM_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.film.LikeAlreadyExistsException.LIKE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.film.LikeNotFoundException.LIKE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Service("InMemoryFilmService")
public class InMemoryFilmService implements FilmService {
    private final Storage<Film> filmStorage;
    private final Storage<User> userStorage;

    @Autowired
    public InMemoryFilmService(@Qualifier("InMemoryFilmStorage") Storage<Film> filmStorage,
                               @Qualifier("InMemoryUserStorage") Storage<User> userStorage) {
        log.debug("InMemoryFilmService({}, {})",
                filmStorage.getClass().getSimpleName(),
                userStorage.getClass().getSimpleName());
        this.filmStorage = filmStorage;
        log.info(DEPENDENCY_MESSAGE, filmStorage.getClass().getName());
        this.userStorage = userStorage;
        log.info(DEPENDENCY_MESSAGE, userStorage.getClass().getName());
    }

    @Override
    public Film add(Film film) {
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public Film get(long filmID) {
        return filmStorage.get(filmID);
    }

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        log.debug("getPopularFilms({}).", count);
        List<Film> popularFilms = filmStorage.getAll().stream()
                        .sorted(Comparator.comparingInt(this::getLikeCount).reversed())
                        .limit(count)
                        .collect(Collectors.toList());
        log.trace("Возвращены популярные фильмы: {}.", popularFilms);
        return popularFilms;
    }

    @Override
    public void addLike(long filmID, long userID) {
        log.debug("addLike({}, {}).", filmID, userID);
        if (!filmStorage.contains(filmID)) {
            log.warn("Не удалось добавить лайк: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.contains(userID)) {
            log.warn("Не удалось добавить лайк: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (filmStorage.get(filmID).getLikes().contains(userID)) {
            log.warn("Не удалось добавить лайк: {}.", format(LIKE_ALREADY_EXISTS, filmID, userID));
            throw new LikeAlreadyExistsException(format(LIKE_ALREADY_EXISTS, filmID, userID));
        }
        filmStorage.get(filmID).getLikes().add(userID);
        log.debug("Добавлен лайк: {}.", filmStorage.get(filmID));
    }

    @Override
    public void deleteLike(long filmID, long userID) {
        log.debug("deleteLike({}, {}).", filmID, userID);
        if (!filmStorage.contains(filmID)) {
            log.warn("Не удалось удалить лайк: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.contains(userID)) {
            log.warn("Не удалось удалить лайк: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (!filmStorage.get(filmID).getLikes().contains(userID)) {
            log.warn("Не удалось удалить лайк: {}.", format(LIKE_NOT_FOUND, filmID, userID));
            throw new LikeNotFoundException(format(LIKE_NOT_FOUND, filmID, userID));
        }
        filmStorage.get(filmID).getLikes().remove(userID);
        log.debug("Лайк удалён: {}.", filmStorage.get(filmID));
    }

    private int getLikeCount(Film film) {
        log.debug("getLikeCount({}).", film);
        log.trace("Возвращено кол-во лайков: {}.", film.getLikes().size());
        return film.getLikes().size();
    }
}
