package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.service.user.UserLogicException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.service.user.UserLogicException.*;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        log.debug("UserService({}).", userStorage.getClass().getSimpleName());
        this.userStorage = userStorage;
        log.info("Подключена зависимость: {}.", userStorage.getClass().getName());
    }

    public void addFriend(long userID, long friendID) {
        log.debug("addFriend({}, {}).", userID, friendID);
        if (!userStorage.getStorage().containsKey(userID)) {
            log.warn("Друг не добавлен: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (userID == friendID) {
            log.warn("Друг не добавлен: {}.", format(UNABLE_TO_ADD_YOURSELF, userID));
            throw new UserLogicException(format(UNABLE_TO_ADD_YOURSELF, userID));
        }
        if (!userStorage.getStorage().containsKey(friendID)) {
            log.warn("Друг не добавлен: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }
        userStorage.getStorage().get(friendID).getFriends().add(userID);
        log.debug("Пользователь добавлен в друзья друга: {}.", userStorage.getStorage().get(friendID));
        userStorage.getStorage().get(userID).getFriends().add(friendID);
        log.debug("Друг добавлен пользователю в друзья: {}.", userStorage.getStorage().get(userID));
    }

    public void deleteFriend(long userID, long friendID) {
        log.debug("deleteFriend({}, {}).", userID, friendID);
        if (!userStorage.getStorage().containsKey(userID)) {
            log.warn("Друг не удалён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (userID == friendID) {
            log.warn("Друг не удалён: {}.", format(UNABLE_TO_DELETE_YOURSELF, userID));
            throw new UserLogicException(format(UNABLE_TO_DELETE_YOURSELF, userID));
        }
        if (!userStorage.getStorage().containsKey(friendID)) {
            log.warn("Друг не удалён: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }
        userStorage.getStorage().get(friendID).getFriends().remove(userID);
        log.debug("Пользователь удалён из друзей друга: {}.", userStorage.getStorage().get(friendID));
        userStorage.getStorage().get(userID).getFriends().remove(friendID);
        log.debug("Друг удалён из друзей пользователя: {}.", userStorage.getStorage().get(userID));
    }

    public Collection<User> getFriends(long userID) {
        log.debug("getFriends({}).", userID);
        if (!userStorage.getStorage().containsKey(userID)) {
            log.warn("Список друзей не вовзращён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        var friends =
                userStorage.getStorage()
                        .get(userID)
                        .getFriends()
                        .stream()
                        .map(userStorage::get)
                        .collect(Collectors.toList());
        log.trace("Возвращён список друзей: {}.", friends);
        return friends;
    }

    public Collection<User> getMutualFriends(long userID, long friendID) {
        log.debug("getMutualFriends({}, {}).", userID, friendID);
        if (!userStorage.getStorage().containsKey(userID)) {
            log.warn("Список общих друзей не вовзращён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (userID == friendID) {
            log.warn("Список общих друзей не вовзращён: {}.", format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
            throw new UserLogicException(format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
        }
        if (!userStorage.getStorage().containsKey(friendID)) {
            log.warn("Список общих друзей не вовзращён: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }

        var mutualFriends = CollectionUtils.intersection(
                        userStorage.get(userID).getFriends(),
                        userStorage.get(friendID).getFriends())
                .stream()
                .map(userStorage::get)
                .collect(Collectors.toList());

        log.trace("Возвращён список общих друзей: {}.", mutualFriends);
        return mutualFriends;
    }
}
