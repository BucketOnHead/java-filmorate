package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.storage.film.LikeAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.film.LikeNotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException.FILM_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.film.LikeAlreadyExistsException.LIKE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.film.LikeNotFoundException.LIKE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        log.debug("FilmService({}, {})",
                filmStorage.getClass().getSimpleName(),
                userStorage.getClass().getSimpleName());
        this.filmStorage = filmStorage;
        log.info("Подключена зависимость: {}.", filmStorage.getClass().getName());
        this.userStorage = userStorage;
        log.info("Подключена зависимость: {}.", userStorage.getClass().getName());
    }

    public Collection<Film> getPopularFilms(int count) {
        log.debug("getPopularFilms({}).", count);
        var popularFilms =
                filmStorage.getAll()
                        .stream()
                        .sorted(Comparator.comparingInt(this::getLikeCount).reversed())
                        .limit(count)
                        .collect(Collectors.toList());
        log.trace("Возвращены популярные фильмы: {}.", popularFilms);
        return popularFilms;
    }

    public void addLike(long filmID, long userID) {
        log.debug("addLike({}, {}).", filmID, userID);
        if (!filmStorage.getStorage().containsKey(filmID)) {
            log.warn("Лайк не добавлен: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.getStorage().containsKey(userID)) {
            log.warn("Лайк не добавлен: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (filmStorage.getStorage().get(filmID).getLikes().contains(userID)) {
            log.warn("Лайк не добавлен: {}.", format(LIKE_ALREADY_EXISTS, filmID, userID));
            throw new LikeAlreadyExistsException(format(LIKE_ALREADY_EXISTS, filmID, userID));
        }
        filmStorage.getStorage().get(filmID).getLikes().add(userID);
        log.debug("Добавлен лайк: {}.", filmStorage.getStorage().get(filmID));
    }

    public void deleteLike(long filmID, long userID) {
        log.debug("deleteLike({}, {}).", filmID, userID);
        if (!filmStorage.getStorage().containsKey(filmID)) {
            log.warn("Лайк не удалён: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.getStorage().containsKey(userID)) {
            log.warn("Лайк не удалён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (!filmStorage.getStorage().get(filmID).getLikes().contains(userID)) {
            log.warn("Лайк не удалён: {}.", format(LIKE_NOT_FOUND, userID, filmID));
            throw new LikeNotFoundException(format(LIKE_NOT_FOUND, userID, filmID));
        }
        filmStorage.getStorage().get(filmID).getLikes().remove(userID);
        log.debug("Лайк удалён: {}.", filmStorage.getStorage().get(filmID));
    }

    private int getLikeCount(Film film) {
        log.trace("getLikeCount({}).", film);
        log.trace("Возвращено кол-во лайков: {}.", film.getLikes().size());
        return film.getLikes().size();
    }
}
