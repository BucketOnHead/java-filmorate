package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.ValidatorException;
import ru.yandex.practicum.filmorate.validator.film.FilmValidator;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        try {
            FilmValidator.validate(film);
            filmStorage.addFilm(film);
            log.trace("Успешно добавлен фильм: {}.", film);
        } catch (ValidatorException e) {
            log.warn("Фильм не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        }

        return film;
    }

    public Film updateFilm(Film film) {
        try {
            FilmValidator.validate(film);
            filmStorage.updateFilm(film);
            log.trace("Фильм успешно обновлён: {}.", film);
        } catch (ValidatorException e) {
            log.warn("Не удалось обновить фильм: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        }

        return film;
    }

    public List<Film> findAll() {
        log.trace("Возвращены все фильмы.");
        return filmStorage.getFilms();
    }
}
