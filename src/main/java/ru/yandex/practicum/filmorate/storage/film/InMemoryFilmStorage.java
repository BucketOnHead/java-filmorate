package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.exception.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.storage.film.exception.FilmAlreadyExistsException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.storage.film.exception.FilmNotFoundException.FILM_NOT_FOUND;
import static ru.yandex.practicum.filmorate.storage.film.exception.LikeAlreadyExistsException.LIKE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.storage.film.exception.LikeNotFoundException.LIKE_NOT_FOUND;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long uniqueFilmID = 1;

    @Override
    public Film addFilm(Film film) {
        log.debug("addFilm({}).", film);
        if (films.containsKey(film.getId())) {
            log.warn("Фильм не добавлен: {}", format(FILM_ALREADY_EXISTS, film.getId()));
            throw new FilmAlreadyExistsException(format(FILM_ALREADY_EXISTS, film.getId()));
        }
        registerFilm(film);
        log.debug("Фильму присвоен ID_{}.", film.getId());
        films.put(film.getId(), film);
        log.trace("Фильм добавлен: {}.", film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("updateFilm({}).", film);
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм не обновлён: {}.", format(FILM_NOT_FOUND, film.getId()));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, film.getId()));
        }
        films.put(film.getId(), film);
        log.debug("Фильм обновлён: {}.", film);
        return films.get(film.getId());
    }

    @Override
    public void deleteFilm(Film film) {
        log.debug("deleteFilm({}).", film);
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм не удалён: {}.", format(FILM_NOT_FOUND, film.getId()));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, film.getId()));
        }
        films.remove(film.getId());
        log.trace("Фильм удалён.");
    }

    @Override
    public Film getFilm(long filmID) {
        log.debug("getFilm({}).", filmID);
        if (!films.containsKey(filmID)) {
            log.warn("Фильм не возвращён: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }
        log.trace("Фильм возвращён.");
        return films.get(filmID);
    }

    @Override
    public List<Film> getFilms() {
        log.trace("Возвращены все фильмы.");
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLikeForFilm(long filmID, long userID) {
        log.debug("addLikeForFilm({}, {}).", filmID, userID);
        if (!films.containsKey(filmID)) {
            log.warn("Лайк не добавлен: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }

        if (films.get(filmID).getLikes().contains(userID)) {
            log.warn("Лайк не добавлен: {}.", format(LIKE_ALREADY_EXISTS, filmID, userID));
            throw new LikeAlreadyExistsException(format(LIKE_ALREADY_EXISTS, filmID, userID));
        }

        films.get(filmID).getLikes().add(userID);
        log.debug("Лайк добавлен: {}.", films.get(filmID));
    }

    @Override
    public void deleteLikeForFilm(long filmID, long userID) {
        log.debug("deleteLikeForFilm({}, {}).", filmID, userID);
        if (!films.containsKey(filmID)) {
            log.warn("Лайк не удалён: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }

        if (!films.get(filmID).getLikes().contains(userID)) {
            log.warn("Лайк не удалён: {}.", format(LIKE_NOT_FOUND, userID, filmID));
            throw new LikeNotFoundException(format(LIKE_NOT_FOUND, userID, filmID));
        }

        films.get(filmID).getLikes().remove(userID);
        log.debug("Лайк удалён: {}.", films.get(filmID));
    }

    @Override
    public List<Film> getTopNFilms(int topN) {
        log.debug("getTopNFilms({}).", topN);
        return films.values()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(topN)
                .collect(Collectors.toList());
    }

    private void registerFilm(Film film) {
        film.setId(uniqueFilmID++);
    }
}
