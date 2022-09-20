package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validator.film.FilmValidator;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        log.debug("Подключена зависимость: {}.", filmStorage.getClass().getName());
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        FilmValidator.validate(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        FilmValidator.validate(film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(long filmID) {
        return filmStorage.getFilm(filmID);
    }

    public List<Film> getTopNFilms(int topN) {
        return filmStorage.getTopNFilms(topN);
    }

    public void addLikeForFilm(long filmID, long userID) {
        filmStorage.addLikeForFilm(filmID, userID);
    }

    public void deleteLikeForFilm(long filmID, long userID) {
        filmStorage.deleteLikeForFilm(filmID, userID);
    }
}
