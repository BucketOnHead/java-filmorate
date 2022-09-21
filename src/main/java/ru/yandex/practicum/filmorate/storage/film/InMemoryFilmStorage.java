package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmAlreadyExistsException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException.FILM_NOT_FOUND;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();
    private long uniqueFilmID = 1;

    @Override
    public Film add(Film film) {
        log.debug("addFilm({}).", film);
        validate(film);
        if (storage.containsKey(film.getId())) {
            log.warn("Фильм не добавлен: {}", format(FILM_ALREADY_EXISTS, film.getId()));
            throw new FilmAlreadyExistsException(format(FILM_ALREADY_EXISTS, film.getId()));
        }
        registerFilm(film);
        storage.put(film.getId(), film);
        log.trace("Добавлен фильм: {}.", film);
        return storage.get(film.getId());
    }

    @Override
    public Film update(Film film) {
        log.debug("updateFilm({}).", film);
        validate(film);
        if (!storage.containsKey(film.getId())) {
            log.warn("Фильм не обновлён: {}.", format(FILM_NOT_FOUND, film.getId()));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, film.getId()));
        }
        storage.put(film.getId(), film);
        log.trace("Обновлён фильм: {}.", film);
        return storage.get(film.getId());
    }

    @Override
    public Film get(long filmID) {
        log.debug("getFilm({}).", filmID);
        if (!storage.containsKey(filmID)) {
            log.warn("Фильм не возвращён: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }
        log.trace("Возвращён фильм: {}", storage.get(filmID));
        return storage.get(filmID);
    }

    @Override
    public Map<Long, Film> getStorage() {
        log.debug("getStorage().");
        log.trace("Возвращено внутренне хранилище: {}.", storage);
        return storage;
    }

    @Override
    public Collection<Film> getAll() {
        log.debug("getFilms().");
        log.trace("Возвращены все фильмы: {}.", storage.values());
        return storage.values();
    }

    private void validate(@NonNull Film film) {
        log.debug("validate({}).", film);
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Валидация не пройдена: {}", "Дата фильма не может быть раньше 28.12.1895.");
            throw new ValidationException("Дата фильма не может быть раньше 28.12.1895");
        }
    }

    private void registerFilm(Film film) {
        log.trace("registerFilm({}).", film);
        film.setId(uniqueFilmID++);
        log.debug("Фильму присвоен ID_{}: {}.", film.getId(), film);
    }
}
