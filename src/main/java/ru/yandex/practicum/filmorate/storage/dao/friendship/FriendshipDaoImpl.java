package ru.yandex.practicum.filmorate.storage.dao.friendship;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    private static final RowMapper<Friendship> friendshipMapper = new BeanPropertyRowMapper<>(Friendship.class);
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
        jdbcTemplate.update(""
                + "INSERT INTO friendships (from_user_id, to_user_id, isMutual) "
                + "VALUES(?, ?, ?)", fromUserID, toUserID, isMutual);
        Friendship result = get(fromUserID, toUserID);
        log.trace("Добавлена связь: {}.", result);
    }

    @Override
    public void delete(long fromUserID, long toUserID) {
        log.debug("delete({}, {}).", fromUserID, toUserID);
        Friendship result = Objects.requireNonNull(get(fromUserID, toUserID));
        jdbcTemplate.update(""
                + "DELETE FROM friendships "
                + "WHERE from_user_id=? "
                + "AND to_user_id=?", fromUserID, toUserID);
        if (result.getIsMutual()) {
            jdbcTemplate.update(""
                    + "UPDATE friendships "
                    + "SET isMutual=false "
                    + "WHERE from_user_id=? "
                    + "AND to_user_id=?", toUserID, fromUserID);
            log.debug("Дружба между {} и {} перестала быть взаимной.",
                    toUserID, fromUserID);
        }
        log.trace("Удалена связь: {}.", result);
    }

    @Override
    public Collection<Long> getFromUserIDs(long toUserId) {
        log.debug("getFriendships({}).", toUserId);
        List<Long> friendships = jdbcTemplate.query(format(""
                        + "SELECT from_user_id, to_user_id, IsMutual "
                        + "FROM friendships "
                        + "WHERE to_user_id=%d", toUserId), friendshipMapper)
                .stream()
                .map(Friendship::getFromUserId)
                .collect(Collectors.toList());
        log.trace("Возвращены запросы на дружбу с пользователем ID_{}: {}.",
                toUserId, friendships);
        return friendships;
    }

    @Override
    public boolean contains(long fromUserID, long toUserID) {
        log.debug("contains({}, {}).", fromUserID, toUserID);
        try {
            get(fromUserID, toUserID);
            log.trace("Найден запрос на дружбу от пользователя ID_{} к пользователю ID_{}.",
                    fromUserID, toUserID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Запрос на дружбу от пользователя ID_{} к пользователю ID_{} не найден.",
                    fromUserID, toUserID);
            return false;
        }
    }

    /**
     * Метод возвращает связь между пользователями
     *
     * @param fromUserID идентификатор пользователя,
     *                   отправившего запрос на дружбу.
     * @param toUserID   идентификатор пользователя,
     *                   которому был отправлен запрос
     *                   на дружбу.
     * @return Связь между пользователями.
     */
    private Friendship get(long fromUserID, long toUserID) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT from_user_id, to_user_id, isMutual "
                + "FROM friendships "
                + "WHERE from_user_id=%d "
                + "AND to_user_id=%d", fromUserID, toUserID), friendshipMapper);
    }
}
