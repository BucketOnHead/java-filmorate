package ru.yandex.practicum.filmorate.storage.dao.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        log.debug("MpaDaoImpl({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public Mpa get(int mpaID) {
        log.debug("get({}).", mpaID);
        Mpa mpa = jdbcTemplate.queryForObject(format(""
                + "SELECT mpa_rating_id, name "
                + "FROM mpa_ratings "
                + "WHERE mpa_rating_id=%d", mpaID), new MpaMapper());
        log.trace("Возвращён рейтинг MPA: {}.", mpa);
        return mpa;
    }

    @Override
    public Collection<Mpa> getAll() {
        log.debug("getAll().");
        List<Mpa> result = jdbcTemplate.query(""
                        + "SELECT mpa_rating_id, name "
                        + "FROM mpa_ratings "
                        + "ORDER BY mpa_rating_id", new MpaMapper());
        log.trace("Возвращены все рейтинги MPA: {}.", result);
        return result;
    }

    @Override
    public boolean contains(int mpaID) {
        log.debug("contains({}).", mpaID);
        try {
            get(mpaID);
            log.trace("Рейтинг MPA ID_{} найден.", mpaID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Рейтинг MPA ID_{} не найден.", mpaID);
            return false;
        }
    }

    private static class MpaMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Mpa(
                    rs.getInt("mpa_rating_id"),
                    rs.getString("name"));
        }
    }
}
