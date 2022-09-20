package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.exception.LogicException;
import ru.yandex.practicum.filmorate.storage.user.exception.UserAlreadyExistsException;
import ru.yandex.practicum.filmorate.storage.user.exception.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.storage.user.exception.LogicException.*;
import static ru.yandex.practicum.filmorate.storage.user.exception.UserAlreadyExistsException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.storage.user.exception.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long uniqueUserID = 1;

    @Override
    public User addUser(User user) {
        log.debug("addUser({}).", user);
        if (users.containsKey(user.getId())) {
            log.warn("Пользователь не добавлен: {}", format(USER_ALREADY_EXISTS, user.getId()));
            throw new UserAlreadyExistsException(format(USER_ALREADY_EXISTS, user.getId()));
        }
        registerUser(user);
        log.debug("Пользователю присвоен ID_{}.", user.getId());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен: {}.", user);
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user) {
        log.debug("updateUser({}).", user);
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь не обновлён: {}", format(USER_NOT_FOUND, user.getId()));
            throw new UserNotFoundException(format(USER_NOT_FOUND, user.getId()));
        }
        users.put(user.getId(), user);
        log.debug("Пользователь обновлён: {}.", user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUser(User user) {
        log.debug("deleteUser({}).", user);
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь не удалён: {}.", format(USER_NOT_FOUND, user.getId()));
            throw new UserNotFoundException(format(USER_NOT_FOUND, user.getId()));
        }
        users.remove(user.getId());
        log.trace("Пользователь удалён: {}.", user);
    }

    @Override
    public User getUser(long userID) {
        log.debug("getUser({}).", userID);
        if (!users.containsKey(userID)) {
            log.warn("Пользователь не возвращён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        log.trace("Пользователь возвращён.");
        return users.get(userID);
    }

    @Override
    public List<User> getUsers() {
        log.trace("Возвращены все пользователи.");
        return new ArrayList<>(users.values());
    }

    @Override
    public void addUserFriend(long userID, long friendID) {
        log.debug("addUserFriend({}, {}).", userID, friendID);
        if (!users.containsKey(userID)) {
            log.warn("Друг не добавлен: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }

        if (userID == friendID) {
            log.warn("Друг не добавлен: {}.", format(UNABLE_TO_ADD_YOURSELF, userID));
            throw new LogicException(format(UNABLE_TO_ADD_YOURSELF, userID));
        }

        if (!users.containsKey(friendID)) {
            log.warn("Друг не добавлен: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }

        users.get(friendID).getFriends().add(userID);
        log.debug("Пользователь добавлен в друзья друга: {}.", users.get(friendID));
        users.get(userID).getFriends().add(friendID);
        log.debug("Друг добавлен пользователю в друзья: {}.", users.get(userID));
    }

    @Override
    public void deleteUserFriend(long userID, long friendID) {
        log.debug("deleteUserFriend({}, {}).", userID, friendID);
        if (!users.containsKey(userID)) {
            log.warn("Друг не удалён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }

        if (userID == friendID) {
            log.warn("Друг не удалён: {}.", format(UNABLE_TO_DELETE_YOURSELF, userID));
            throw new LogicException(format(UNABLE_TO_DELETE_YOURSELF, userID));
        }

        if (!users.containsKey(friendID)) {
            log.warn("Друг не удалён: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }

        users.get(friendID).getFriends().remove(userID);
        log.debug("Пользователь удалён из друзей друга: {}.", users.get(friendID));
        users.get(userID).getFriends().remove(friendID);
        log.debug("Друг удалён из друзей пользователя: {}.", users.get(userID));
    }

    @Override
    public List<User> getUserFriends(long userID) {
        log.debug("getUserFriends({}).", userID);
        if (!users.containsKey(userID)) {
            log.warn("Список друзей не вовзращён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }

        log.trace("Список друзей возвращён.");
        return users.get(userID)
                .getFriends()
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getMutualFriends(long userID, long friendID) {
        log.debug("getMutualFriends({}, {}).", userID, friendID);
        if (!users.containsKey(userID)) {
            log.warn("Список общих друзей не вовзращён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }

        if (userID == friendID) {
            log.warn("Список общих друзей не вовзращён: {}.", format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
            throw new LogicException(format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
        }

        if (!users.containsKey(friendID)) {
            log.warn("Список общих друзей не вовзращён: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }

        log.trace("Список общих друзей возвращён.");
        return CollectionUtils.intersection(
                        users.get(userID).getFriends(),
                        users.get(friendID).getFriends())
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    private void registerUser(User user) {
        user.setId(uniqueUserID++);
    }
}
