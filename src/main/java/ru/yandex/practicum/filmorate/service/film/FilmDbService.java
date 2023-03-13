package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.storage.dao.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.storage.dao.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.storage.film.LikeAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.film.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.storage.dao.genre.GenreNotFoundException.GENRE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.dao.mpa.MpaNotFoundException.MPA_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmAlreadyExistsException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException.FILM_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.film.LikeAlreadyExistsException.LIKE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.film.LikeNotFoundException.LIKE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDbService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final MpaDao mpaDao;

    @Override
    public Film add(Film film) {
        checkFilmToAdd(film);
        Film result = filmStorage.add(film);
        filmStorage.addGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.getGenres(result.getId()));
        return result;
    }

    @Override
    public Film update(Film film) {
        checkFilmToUpdate(film);
        Film result = filmStorage.update(film);
        filmStorage.updateGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.getGenres(result.getId()));
        result.setMpa(mpaDao.get(result.getMpa().getId()));
        return result;
    }

    @Override
    public Film get(long filmID) {
        if (!filmStorage.contains(filmID)) {
            log.warn("Не удалось вернуть фильм: {}.", String.format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, filmID));
        }
        Film film = filmStorage.get(filmID);
        film.setGenres(filmStorage.getGenres(filmID));
        film.setMpa(mpaDao.get(film.getMpa().getId()));
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        var films = filmStorage.getAll();
        for (Film film : films) {
            film.setGenres(filmStorage.getGenres(film.getId()));
            film.setMpa(mpaDao.get(film.getMpa().getId()));
        }
        return films;
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        log.debug("getPopularFilms({}).", count);
        List<Film> popularFilms = filmStorage.getAll().stream()
                .sorted(this::likeCompare)
                .limit(count)
                .collect(Collectors.toList());
        log.trace("Возвращены популярные фильмы: {}.", popularFilms);
        return popularFilms;
    }

    @Override
    public void addLike(long filmID, long userID) {
        checkLikeToAdd(filmID, userID);
        likeDao.add(filmID, userID);
    }

    @Override
    public void deleteLike(long filmID, long userID) {
        checkLikeToDelete(filmID, userID);
        likeDao.delete(filmID, userID);
    }

    /**
     * Метод сравнивает два фильма по количеству
     * лайков (в убывающем порядке).
     *
     * @param film      первый film для сравнения.
     * @param otherFilm второй film для сравнения.
     * @return Значение 0, если количество лайков
     * одинаковое; Значение меньше 0, если у первого
     * больше лайков, чем у второго; И значение
     * больше 0, если у второго больше лайков, чем
     * у первого.
     */
    private int likeCompare(Film film, Film otherFilm) {
        return Integer.compare(likeDao.count(otherFilm.getId()), likeDao.count(film.getId()));
    }

    /**
     * Метод проверяет корректность фильма,
     * для последующего добавления в хранилище.
     *
     * @param film объект фильма.
     * @throws FilmAlreadyExistsException в случае, если фильм
     *                                    уже присутствует в хранилище.
     * @throws IllegalArgumentException   в случае, если фильму
     *                                    задан идентификатор,
     *                                    отличающийся от 0.
     * @throws MpaNotFoundException       в случае, если идентификатор
     *                                    рейтинга MPA не найден.
     * @throws GenreNotFoundException     в случае, если идентификатор
     *                                    жанра не найден.
     */
    private void checkFilmToAdd(Film film) {
        log.debug("checkFilmToAdd({}).", film);
        String msg = "Не удалось добавить фильм: {}.";
        if (film.getId() != 0) {
            if (filmStorage.contains(film.getId())) {
                log.warn(msg, String.format(FILM_ALREADY_EXISTS, film.getId()));
                throw new FilmAlreadyExistsException(String.format(FILM_ALREADY_EXISTS, film.getId()));
            } else {
                log.warn(msg, "Запрещено устанавливать ID вручную");
                throw new IllegalArgumentException("Запрещено устанавливать ID вручную");
            }
        }
        if (!mpaDao.contains(film.getMpa().getId())) {
            log.warn(msg, String.format(MPA_NOT_FOUND, film.getMpa().getId()));
            throw new MpaNotFoundException(String.format(MPA_NOT_FOUND, film.getMpa().getId()));
        }
        for (Genre genre : film.getGenres()) {
            if (!genreDao.contains(genre.getId())) {
                log.warn(msg, String.format(GENRE_NOT_FOUND, genre.getId()));
                throw new GenreNotFoundException(String.format(GENRE_NOT_FOUND, genre.getId()));
            }
        }
    }

    /**
     * Метод проверяет корректность фильма,
     * для последующего обновления в хранилище.
     *
     * @param film объект фильма.
     * @throws FilmNotFoundException  в случае, если фильм
     *                                отсутствует в хранилище.
     * @throws MpaNotFoundException   в случае, если идентификатор
     *                                рейтинга MPA не найден.
     * @throws GenreNotFoundException в случае, если идентификатор
     *                                жанра не найден.
     */
    private void checkFilmToUpdate(Film film) {
        log.debug("checkFilmToUpdate({}).", film);
        String msg = "Не удалось обновить фильм: {}.";
        if (!filmStorage.contains(film.getId())) {
            log.warn(msg, String.format(FILM_NOT_FOUND, film.getId()));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, film.getId()));
        }
        if (!mpaDao.contains(film.getMpa().getId())) {
            log.warn(msg, String.format(MPA_NOT_FOUND, film.getMpa().getId()));
            throw new MpaNotFoundException(String.format(MPA_NOT_FOUND, film.getMpa().getId()));
        }
        for (Genre genre : film.getGenres()) {
            if (!genreDao.contains(genre.getId())) {
                log.warn(msg, String.format(GENRE_NOT_FOUND, genre.getId()));
                throw new GenreNotFoundException(String.format(GENRE_NOT_FOUND, genre.getId()));
            }
        }
    }

    /**
     * Метод проверяет корректность лайка
     * пользователя, для последующего добавления
     * в хранилище.
     *
     * @param filmID идентификатор фильма, которому
     *               пользователь хочет поставить лайк.
     * @param userID идентификатор пользователя, который
     *               хочет поставить лайк.
     * @throws FilmNotFoundException      в случае, если фильм
     *                                    отсутствует в хранилище.
     * @throws UserNotFoundException      в случае, если пользователь
     *                                    отсутствует в хранилище.
     * @throws LikeAlreadyExistsException в случае, если пользователь
     *                                    уже ставил лайк фильму.
     */
    private void checkLikeToAdd(long filmID, long userID) {
        log.debug("checkLikeToAdd({}, {}).", filmID, userID);
        String msg = "Не удалось добавить лайк: {}.";
        if (!filmStorage.contains(filmID)) {
            log.warn(msg, String.format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.contains(userID)) {
            log.warn(msg, String.format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userID));
        }
        if (likeDao.contains(filmID, userID)) {
            log.warn(msg, String.format(LIKE_ALREADY_EXISTS, filmID, userID));
            throw new LikeAlreadyExistsException(String.format(LIKE_ALREADY_EXISTS, filmID, userID));
        }
    }

    /**
     * Метод проверяет корректность лайка
     * пользователя, для последующего добавления
     * в хранилище.
     *
     * @param filmID идентификатор фильма, которому
     *               пользователь хочет поставить лайк.
     * @param userID идентификатор пользователя, который
     *               хочет поставить лайк.
     * @throws FilmNotFoundException в случае, если фильм
     *                               отсутствует в хранилище.
     * @throws UserNotFoundException в случае, если пользователь
     *                               отсутствует в хранилище.
     * @throws LikeNotFoundException в случае, если пользователь
     *                               не ставил лайк фильму.
     */
    private void checkLikeToDelete(long filmID, long userID) {
        log.debug("checkLikeToDelete({}, {}).", filmID, userID);
        String msg = "Не удалось удалить лайк: {}.";
        if (!filmStorage.contains(filmID)) {
            log.warn(msg, String.format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.contains(userID)) {
            log.warn(msg, String.format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userID));
        }
        if (!likeDao.contains(filmID, userID)) {
            log.warn(msg, String.format(LIKE_NOT_FOUND, filmID, userID));
            throw new LikeNotFoundException(String.format(LIKE_NOT_FOUND, filmID, userID));
        }
    }
}
