package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
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
    public User update(User user) {
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
}
