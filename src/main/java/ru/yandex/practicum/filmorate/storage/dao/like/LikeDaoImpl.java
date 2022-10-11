package ru.yandex.practicum.filmorate.storage.dao.like;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.Objects;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        log.debug("LikeDaoImpl({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public void add(long filmID, long userID) {
        log.debug("add({}, {}).", filmID, userID);
        jdbcTemplate.update(""
                + "INSERT INTO film_likes (film_id, user_id) "
                + "VALUES (?, ?)", filmID, userID);
        log.trace("Фильму ID_{} добавлен лайк от пользователя ID_{}.", filmID, userID);
    }

    @Override
    public void delete(long filmID, long userID) {
        log.debug("delete({}, {}).", filmID, userID);
        jdbcTemplate.update(""
                + "DELETE FROM film_likes "
                + "WHERE film_id=? "
                + "AND user_id=?", filmID, userID);
        log.trace("У фильма ID_{} удалён лайк от пользователя ID_{}.", filmID, userID);
    }

    @Override
    public int count(long filmID) {
        log.debug("count({}).", filmID);
        Integer count = Objects.requireNonNull(
                jdbcTemplate.queryForObject(format(""
                        + "SELECT COUNT(*) "
                        + "FROM film_likes "
                        + "WHERE film_id=%d", filmID), Integer.class));
        log.trace("Подсчитано количество лайков для фильма ID_{}: {}.", filmID, count);
        return count;
    }

    @Override
    public boolean contains(long filmID, long userID) {
        log.debug("contains({}, {}).", filmID, userID);
        try {
            jdbcTemplate.queryForObject(format(""
                    + "SELECT film_id, user_id "
                    + "FROM film_likes "
                    + "WHERE film_id=%d "
                    + "AND user_id=%d", filmID, userID), new BeanPropertyRowMapper<>(Like.class));
            log.trace("Найден лайк у фильма ID_{} от пользователя ID_{}.", filmID, userID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.warn("Не найден лайк у фильма ID_{} от пользователя ID_{}.", filmID, userID);
            return false;
        }
    }
}
