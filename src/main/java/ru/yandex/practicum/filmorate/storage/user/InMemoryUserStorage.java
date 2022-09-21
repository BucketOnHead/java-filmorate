package ru.yandex.practicum.filmorate.storage.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.storage.user.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException;

import java.util.*;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserAlreadyExistsException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> userStorage = new HashMap<>();
    private long uniqueUserID = 1;

    @Override
    public User add(User user) {
        log.debug("addUser({}).", user);
        validate(user);
        if (userStorage.containsKey(user.getId())) {
            log.warn("Пользователь не добавлен: {}", format(USER_ALREADY_EXISTS, user.getId()));
            throw new UserAlreadyExistsException(format(USER_ALREADY_EXISTS, user.getId()));
        }
        registerUser(user);
        userStorage.put(user.getId(), user);
        log.debug("Добавлен пользователь: {}.", user);
        return userStorage.get(user.getId());
    }

    @Override
    public User update(User user) {
        log.debug("updateUser({}).", user);
        validate(user);
        if (!userStorage.containsKey(user.getId())) {
            log.warn("Пользователь не обновлён: {}", format(USER_NOT_FOUND, user.getId()));
            throw new UserNotFoundException(format(USER_NOT_FOUND, user.getId()));
        }
        userStorage.put(user.getId(), user);
        log.debug("Обновлён пользователь: {}.", user);
        return userStorage.get(user.getId());
    }

    @Override
    public User get(long userID) {
        log.debug("getUser({}).", userID);
        if (!userStorage.containsKey(userID)) {
            log.warn("Пользователь не возвращён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        log.trace("Возвращён пользователь: {}", userStorage.get(userID));
        return userStorage.get(userID);
    }

    @Override
    public Map<Long, User> getStorage() {
        log.debug("getStorage().");
        log.trace("Возвращено внутренне хранилище: {}", userStorage);
        return userStorage;
    }

    @Override
    public Collection<User> getAll() {
        log.debug("getUsers().");
        log.trace("Возврашены все пользователи: {}", userStorage.values());
        return userStorage.values();
    }

    private void validate(@NonNull User user) {
        log.trace("validate({}).", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Пользователю присвоено имя {}: {}.", user.getLogin(), user);
        }
    }

    private void registerUser(User user) {
        log.trace("registerUser({}).", user);
        user.setId(uniqueUserID++);
        log.debug("Пользователю присвоен ID_{}: {}.", user.getId(), user);
    }
}
