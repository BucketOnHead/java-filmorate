package ru.yandex.practicum.filmorate.storage.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.service.Service.DEPENDENCY_MESSAGE;

@Slf4j
@Component("UserDbStorage")
public class UserDbStorage implements Storage<User> {
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
        jdbcTemplate.update(""
                        + "INSERT INTO users (email, login, name, birthday) "
                        + "VALUES (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        User result = jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE email='%s'", user.getEmail()), new UserMapper());
        log.trace("В хранилище добавлен пользователь: {}.", result);
        return result;
    }

    @Override
    public User update(@NonNull User user) {
        log.debug("update({}).", user);
        jdbcTemplate.update(""
                        + "UPDATE users "
                        + "SET email=?, login=?, name=?, birthday=? "
                        + "WHERE user_id=?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        User result = get(user.getId());
        log.trace("Обновлён пользователь: {}", result);
        return result;
    }

    @Override
    public User get(long userID) {
        log.debug("get({}).", userID);
        User user = jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE user_id=%d", userID), new UserMapper());
        log.trace("Возвращён пользователь: {}", user);
        return user;
    }

    @Override
    public Collection<User> getAll() {
        log.debug("getAll()");
        List<User> users = jdbcTemplate.query(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users", new UserMapper());
        log.trace("Возвращены все пользователи: {}.", users);
        return users;
    }

    @Override
    public boolean contains(long userID) {
        log.debug("contains({}).", userID);
        try {
            get(userID);
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
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            return user;
        }
    }
}
