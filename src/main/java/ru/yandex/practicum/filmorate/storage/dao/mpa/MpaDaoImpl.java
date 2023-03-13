package ru.yandex.practicum.filmorate.storage.dao.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

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
}
