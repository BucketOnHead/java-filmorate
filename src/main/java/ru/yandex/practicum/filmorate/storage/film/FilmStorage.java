package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    Film getFilm(long filmID);

    List<Film> getFilms();

    void addLikeForFilm(long filmID, long userID);

    void deleteLikeForFilm(long filmID, long userID);

    List<Film> getTopNFilms(int n);
}
