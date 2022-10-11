package ru.yandex.practicum.filmorate.storage.dao.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.Friendship;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component
public class FriendshipDaoImpl implements FriendshipDao {
    private static final String SQL_ADD_FRIENDSHIP = ""
            + "INSERT INTO friendships (from_user_id, to_user_id, isMutual) "
            + "VALUES(?, ?, ?)";
    private static final String SQL_GET_FRIENDSHIP = ""
            + "SELECT from_user_id, to_user_id, isMutual "
            + "FROM friendships "
            + "WHERE from_user_id=%d "
            + "AND to_user_id=%d";
    private static final String SQL_DELETE_FRIENDSHIP = ""
            + "DELETE FROM friendships "
            + "WHERE from_user_id=? "
            + "AND to_user_id=?";
    private static final String SQL_UPDATE_IS_MUTUAL = ""
            + "UPDATE friendships "
            + "SET isMutual=false "
            + "WHERE from_user_id=? "
            + "AND to_user_id=?";

    private static final String SQL_GET_FRIENDSHIP_WHERE_FROM_USER_ID = ""
            + "SELECT from_user_id, to_user_id, IsMutual "
            + "FROM friendships "
            + "WHERE to_user_id=%d";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        log.debug("FriendshipDaoImpl({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public void add(long fromUserID, long toUserID, boolean isMutual) {
        log.debug("add({}, {}, {}).", fromUserID, toUserID, isMutual);
        jdbcTemplate.update(SQL_ADD_FRIENDSHIP, fromUserID, toUserID, isMutual);
        Friendship result = jdbcTemplate.queryForObject(
                format(SQL_GET_FRIENDSHIP, fromUserID, toUserID),
                new BeanPropertyRowMapper<>(Friendship.class));
        log.trace("Добавлена связь: {}.", result);
    }

    @Override
    public void delete(long fromUserID, long toUserID) {
        log.debug("delete({}, {}).", fromUserID, toUserID);
        Friendship result = Objects.requireNonNull(jdbcTemplate.queryForObject(
                format(SQL_GET_FRIENDSHIP, fromUserID, toUserID),
                new BeanPropertyRowMapper<>(Friendship.class)));
        jdbcTemplate.update(SQL_DELETE_FRIENDSHIP, fromUserID, toUserID);
        if (result.getIsMutual()) {
            jdbcTemplate.update(SQL_UPDATE_IS_MUTUAL, toUserID, fromUserID);
            log.debug("Дружба между {} и {} перестала быть взаимной.", toUserID, fromUserID);
        }
        log.trace("Удалена связь: {}.", result);
    }

    @Override
    public Collection<Long> getFromUserID(long toUserId) {
        log.debug("getFriendships({}).", toUserId);
        List<Long> friendships = jdbcTemplate.query(
                        format(SQL_GET_FRIENDSHIP_WHERE_FROM_USER_ID, toUserId),
                        new BeanPropertyRowMapper<>(Friendship.class)).stream()
                .map(Friendship::getFromUserId)
                .collect(Collectors.toList());
        log.trace("Возврашены запросы на дружбу с пользователем ID_{}: {}.", toUserId, friendships);
        return friendships;
    }

    @Override
    public boolean contains(long fromUserID, long toUserID) {
        log.debug("contains({}, {}).", fromUserID, toUserID);
        try {
            jdbcTemplate.queryForObject(
                    format(SQL_GET_FRIENDSHIP, fromUserID, toUserID),
                    new BeanPropertyRowMapper<>(Friendship.class));
            log.trace("Найден запрос на дружбу от пользователя ID_{} к пользователю ID_{}.",
                    fromUserID, toUserID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Запрос на дружбу от пользователя ID_{} к пользователю ID_{} не найден.",
                    fromUserID, toUserID);
            return false;
        }
    }
}
