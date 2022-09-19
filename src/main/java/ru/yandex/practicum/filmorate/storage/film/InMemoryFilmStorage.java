package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.storage.film.FilmStorageException.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long uniqueFilmID = 0;

    @Override
    public Film addFilm(Film film) throws FilmStorageException {
        if (films.containsKey(film.getId())) {
            throw new FilmStorageException(
                    format(FILM_ALREADY_EXISTS, film.getId()));
        }
        registerFilm(film);
        return films.put(film.getId(), film);
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmStorageException(
                    format(FILM_NOT_FOUND, film.getId()));
        }
        return films.put(film.getId(), film);
    }

    @Override
    public void deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmStorageException(
                    format(FILM_NOT_FOUND, film.getId()));
        }
        films.remove(film.getId());
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film addLikeForFilm(long filmID, long userID) {
        if (!films.containsKey(filmID)) {
            throw new FilmStorageException(
                    format(FILM_NOT_FOUND, filmID));
        }

        if (films.get(filmID).getLikes().contains(userID)) {
            throw new FilmStorageException(
                    format(FILM_ALREADY_EVALUATED, filmID, userID));
        }

        Film film = films.get(filmID);
        film.getLikes().add(userID);
        return film;
    }

    public Film deleteLikeForFilm(long filmID, long userID) {
        if (!films.containsKey(filmID)) {
            throw new FilmStorageException(
                    format(FILM_NOT_FOUND, filmID));
        }

        if (!films.get(filmID).getLikes().contains(userID)) {
            throw new FilmStorageException(
                    format(LIKE_NOT_FOUND, userID, filmID));
        }

        Film film = films.get(filmID);
        film.getLikes().remove(userID);
        return film;
    }

    public List<Film> getTop10Films() {
        return films.values()
                .stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(10)
                .collect(Collectors.toList());
    }

    private void registerFilm(Film film) {
        film.setId(uniqueFilmID++);
    }
}
