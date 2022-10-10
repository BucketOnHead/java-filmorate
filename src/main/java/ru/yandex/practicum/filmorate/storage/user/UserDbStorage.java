package ru.yandex.practicum.filmorate.storage.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.storage.user.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserAlreadyExistsException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException.USER_NOT_FOUND;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements Storage<User> {
    private static final String SQL_ADD_USER = ""
            + "INSERT INTO users (email, login, name, birthday) "
            + "VALUES (?, ?, ?, ?)";
    private static final String SQL_GET_USER_WITH_EMAIL = ""
            + "SELECT user_id, email, login, name, birthday "
            + "FROM users "
            + "WHERE email='%s'";
    private static final String SQL_GET_USER_WITH_ID = ""
            + "UPDATE users "
            + "SET email=?, login=?, name=?, birthday=? "
            + "WHERE user_id=?";
    private static final String SQL_GET_USER_WITH_ID_2 = ""
            + "SELECT user_id, email, login, name, birthday "
            + "FROM users "
            + "WHERE user_id=%d";
    private static final String SQL_GET_ALL = ""
            + "SELECT user_id, email, login, name, birthday "
            + "FROM users";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        log.debug("UserDbStorage({}).", jdbcTemplate.getClass().getSimpleName());
        this.jdbcTemplate = jdbcTemplate;
        log.info(DEPENDENCY_MESSAGE, jdbcTemplate.getClass().getName());
    }

    @Override
    public User add(@NonNull User user) {
        log.debug("add({}).", user);
        if (user.getId() != 0) {
            if (contains(user.getId())) {
                log.warn("Не удалось добавить пользователя: {}.",
                        format(USER_ALREADY_EXISTS, user.getId()));
                throw new UserAlreadyExistsException(format(USER_ALREADY_EXISTS, user.getId()));
            } else {
                log.warn("Не удалось добавить пользователя: {}.", "Запрещено устанавливать ID вручную");
                throw new IllegalArgumentException("Запрещено устанавливать ID вручную");
            }
        }
        jdbcTemplate.update(SQL_ADD_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        User result = jdbcTemplate.queryForObject(
                format(SQL_GET_USER_WITH_EMAIL, user.getEmail()), new UserMapper());
        log.trace("В хранилище добавлен пользователь: {}.", result);
        return result;
    }

    @Override
    public User update(@NonNull User user) {
        log.debug("update({}).", user);
        if (!contains(user.getId())) {
            log.warn("Пользователь не обновлён: {}.", format(USER_NOT_FOUND, user.getId()));
            throw new UserNotFoundException(format(USER_NOT_FOUND, user.getId()));
        }
        jdbcTemplate.update(SQL_GET_USER_WITH_ID,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        User result = Objects.requireNonNull(jdbcTemplate.queryForObject(
                format(SQL_GET_USER_WITH_ID_2, user.getId()), new UserMapper()));
        log.trace("Обновлён пользователь: {}", result);
        return result;

    }

    @Override
    public User get(long userID) {
        log.debug("get({}).", userID);
        if (!contains(userID)) {
            log.warn("Не удалось вернуть пользователя: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        User user = jdbcTemplate.queryForObject(
                format(SQL_GET_USER_WITH_ID_2, userID), new UserMapper());
        log.trace("Возвращён пользователь: {}", user);
        return user;
    }

    @Override
    public Collection<User> getAll() {
        log.debug("getAll()");
        List<User> users = jdbcTemplate.query(SQL_GET_ALL, new UserMapper());
        log.trace("Возвращены все пользователи: {}.", users);
        return users;
    }

    @Override
    public boolean contains(long userID) {
        log.debug("contains({}).", userID);
        try {
            jdbcTemplate.queryForObject(format(SQL_GET_USER_WITH_ID_2, userID), new UserMapper());
            log.trace("Найден пользователь ID_{}.", userID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Не найден пользователь ID_{}.", userID);
            return false;
        }
    }

    private static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("user_id"))
                    .email(rs.getString("email"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();
        }
    }
}
