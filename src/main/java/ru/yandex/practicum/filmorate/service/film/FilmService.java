package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.film.FilmValidator;
import ru.yandex.practicum.filmorate.validator.ValidatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.film.FilmServiceException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.service.film.FilmServiceException.FILM_NOT_FOUND;

@Slf4j
@Service
public class FilmService {
    private final Map<Long, Film> films = new HashMap<>();
    private long uniqueID = 1;

    public Film createFilm(Film film) {
        try {
            FilmValidator.validate(film);

            if (films.containsKey(film.getId())) {
                throw new FilmServiceException(
                        String.format(FILM_ALREADY_EXISTS, film));
            }

            film.setId(uniqueID++);
            films.put(film.getId(), film);
            log.trace("Успешно добавлен фильм: {}.", film);
        } catch (ValidatorException e) {
            log.warn("Фильм не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (FilmServiceException e) {
            log.warn("Фильм не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера:" + e.getMessage(), e);
        } finally {
            log.debug("Количество фильмов: {}.", films.size());
        }
        return film;
    }

    public Film updateFilm(Film film) {
        try {
            FilmValidator.validate(film);

            if (!films.containsKey(film.getId())) {
                throw new FilmServiceException(
                        String.format(FILM_NOT_FOUND, film));
            }

            films.put(film.getId(), film);
            log.trace("Фильм успешно обновлён: {}.", film);
        } catch (ValidatorException e) {
            log.warn("Не удалось обновить фильм: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (FilmServiceException e) {
            log.warn("Не удалось обновить фильм: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера:" + e.getMessage(), e);
        } finally {
            log.debug("Количество фильмов: {}.", films.size());
        }

        return film;
    }

    public List<Film> findAll() {
        log.trace("Возвращены все фильмы.");
        return new ArrayList<>(films.values());
    }
}
