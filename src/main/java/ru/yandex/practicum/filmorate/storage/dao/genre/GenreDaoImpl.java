package ru.yandex.practicum.filmorate.storage.dao.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        log.debug("GenreDaoImpl({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public Genre get(int genreID) {
        log.debug("get({}).", genreID);
        Genre genre = jdbcTemplate.queryForObject(format(""
                + "SELECT genre_id, name "
                + "FROM genres "
                + "WHERE genre_id=%d", genreID), new GenreMapper());
        log.trace("Возвращён жанр: {}.", genre);
        return genre;
    }

    @Override
    public Collection<Genre> getAll() {
        log.debug("getAll().");
        List<Genre> result = jdbcTemplate.query(""
                + "SELECT genre_id, name "
                + "FROM genres "
                + "ORDER BY genre_id", new GenreMapper());
        log.trace("Возвращены все жанры: {}.", result);
        return result;
    }

    @Override
    public boolean contains(int genreID) {
        log.debug("contains({}).", genreID);
        try {
            get(genreID);
            log.trace("Найден жанр ID_{}.", genreID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Не найден жанр ID_{}.", genreID);
            return false;
        }
    }
}
