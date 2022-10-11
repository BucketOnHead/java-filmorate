package ru.yandex.practicum.filmorate.storage.dao.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {
    private static final String SQL_ADD_FILM_GENRES = ""
            + "INSERT INTO film_genres (film_id, genre_id) "
            + "VALUES (?, ?)";
    private static final String SQL_GET_GENRE = ""
            + "SELECT genre_id, name "
            + "FROM genres "
            + "WHERE genre_id=%d";
    private static final String SQL_GET_ALL_ORDER_BY_GENRE_ID = ""
            + "SELECT genre_id, name "
            + "FROM genres "
            + "ORDER BY genre_id";
    private static final String SQL_GET_GENRES_FROM_FILM_ORDER_BY_GENRE = ""
            + "SELECT f.genre_id, g.name "
            + "FROM film_genres AS f "
            + "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id "
            + "WHERE f.film_id=%d "
            + "ORDER BY g.genre_id";
    private static final String SQL_DELETE_FILM_GENRES_WHERE_FILM_ID = ""
            + "DELETE "
            + "FROM film_genres "
            + "WHERE film_id=?";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        log.debug("GenreDaoImpl({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public void add(long filmID, Set<Genre> genres) {
        log.debug("add({}, {})", filmID, genres);
        for (Genre genre : genres) {
            jdbcTemplate.update(SQL_ADD_FILM_GENRES, filmID, genre.getId());
            log.trace("Фильму ID_{} добавлен жанр ID_{}.", filmID, genre.getId());
        }
    }

    @Override
    public void update(long filmID, Set<Genre> genres) {
        log.debug("update({}, {})", filmID, genres);
        delete(filmID);
        add(filmID, genres);
    }

    @Override
    public Genre get(int genreID) {
        log.debug("get({}).", genreID);
        Genre genre = jdbcTemplate.queryForObject(format(SQL_GET_GENRE, genreID), new GenreMapper());
        log.trace("Возвращён жанр: {}.", genre);
        return genre;
    }

    @Override
    public Collection<Genre> getAll() {
        log.debug("getAll().");
        List<Genre> result = jdbcTemplate.query(SQL_GET_ALL_ORDER_BY_GENRE_ID, new GenreMapper());
        log.trace("Возвращенны все жанры: {}.", result);
        return result;
    }

    @Override
    public Set<Genre> getGenres(long filmID) {
        log.debug("getGenres({}).", filmID);
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(
                format(SQL_GET_GENRES_FROM_FILM_ORDER_BY_GENRE, filmID), new GenreMapper()));
        log.trace("Возвращены все жанры для фильма ID_{}: {}.", filmID, genres);
        return genres;
    }

    @Override
    public boolean contains(int genreID) {
        log.debug("contains({}).", genreID);
        try {
            jdbcTemplate.queryForObject(format(SQL_GET_GENRE, genreID), new GenreMapper());
            log.trace("Найден жанр ID_{}.", genreID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Не найден жанр ID_{}.", genreID);
            return false;
        }
    }

    private void delete(long filmID) {
        log.debug("delete({}).", filmID);
        jdbcTemplate.update(SQL_DELETE_FILM_GENRES_WHERE_FILM_ID, filmID);
        log.debug("Удалены все жанры у фильма {}.", filmID);
    }

    static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(
                    rs.getInt("genre_id"),
                    rs.getString("name"));
        }
    }
}
