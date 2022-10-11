package ru.yandex.practicum.filmorate.storage.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmAlreadyExistsException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.film.FilmNotFoundException.FILM_NOT_FOUND;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component("FilmDbStorage")
public class FilmDbStorage implements Storage<Film> {
    private static final String SQL_ADD_FILM = ""
            + "INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_rating_id) "
            + "VALUES(?, ?, ?, ?, ?)";
    private static final String SQL_GET_FILM_WITHOUT_ID = ""
            + "SELECT film_id, name, description, release_date, duration_in_minutes, mpa_rating_id "
            + "FROM films "
            + "WHERE name='%s' "
            + "AND description='%s' "
            + "AND release_date='%s' "
            + "AND duration_in_minutes=%d "
            + "AND mpa_rating_id=%d";
    private static final String SQL_UPDATE_FILM_WITH_ID = ""
            + "UPDATE films SET name=?, description=?, release_date=?, duration_in_minutes=?, mpa_rating_id=?"
            + "WHERE film_id=?";
    private static final String SQL_GET_FILM_WITH_ID = ""
            + "SELECT film_id, name, description, release_date, duration_in_minutes, mpa_rating_id FROM films "
            + "WHERE film_id=%d";
    private static final String SQL_GET_ALL = ""
            + "SELECT film_id, name, description, release_date, duration_in_minutes, mpa_rating_id "
            + "FROM films";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        log.debug("FilmDbStorage({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public Film add(@NonNull Film film) {
        log.debug("add({}).", film);
        if (film.getId() != 0) {
            if (contains(film.getId())) {
                log.warn("Не удалось добавить фильм: {}.", format(FILM_ALREADY_EXISTS, film.getId()));
                throw new FilmAlreadyExistsException(format(FILM_ALREADY_EXISTS, film.getId()));
            } else {
                log.warn("Не удалось добавить фильм: {}.", "Запрещено устанавливать ID вручную");
                throw new IllegalArgumentException("Запрещено устанавливать ID вручную");
            }
        }
        jdbcTemplate.update(SQL_ADD_FILM,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        Film result = Objects.requireNonNull(
                jdbcTemplate.queryForObject(format(SQL_GET_FILM_WITHOUT_ID,
                        film.getName(),
                        film.getDescription(),
                        Date.valueOf(film.getReleaseDate()),
                        film.getDuration(),
                        film.getMpa().getId()), new FilmMapper()));
        log.trace("В хранилище добавлен фильм: {}.", result);
        return result;
    }

    @Override
    public Film update(@NonNull Film film) {
        log.debug("update({}).", film);
        if (!contains(film.getId())) {
            log.warn("Не удалось обновить фильм: {}.", format(FILM_NOT_FOUND, film.getId()));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, film.getId()));
        }
        jdbcTemplate.update(SQL_UPDATE_FILM_WITH_ID,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        Film result = Objects.requireNonNull(jdbcTemplate.queryForObject(
                format(SQL_GET_FILM_WITH_ID, film.getId()), new FilmMapper()));
        log.trace("Обновлён фильм: {}.", result);
        return result;
    }

    @Override
    public Film get(long filmID) {
        log.debug("get({}).", filmID);
        if (!contains(filmID)) {
            log.warn("Не удалось добавить фильм: {}.", format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(format(FILM_NOT_FOUND, filmID));
        }
        Film film = jdbcTemplate.queryForObject(
                format(SQL_GET_FILM_WITH_ID, filmID), new FilmMapper());
        log.trace("Возвращён фильм: {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        log.debug("getAll().");
        List<Film> films = jdbcTemplate.query(SQL_GET_ALL, new FilmMapper());
        log.trace("Возвращены все фильмы: {}.", films);
        return films;
    }

    @Override
    public boolean contains(long filmID) {
        log.debug("contains({}).", filmID);
        try {
            jdbcTemplate.queryForObject(format(SQL_GET_FILM_WITH_ID, filmID), new FilmMapper());
            log.trace("Найден фильм ID_{}.", filmID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Не найден фильм ID_{}.", filmID);
            return false;
        }
    }

    private static class FilmMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpa_rating_id"));

            Film film = new Film();
            film.setId(rs.getLong("film_id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration_in_minutes"));
            film.setMpa(mpa);

            return film;
        }
    }
}

