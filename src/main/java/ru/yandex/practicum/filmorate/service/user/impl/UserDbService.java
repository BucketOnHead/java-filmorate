package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.service.user.UserLogicException;
import ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.dao.friendship.FriendshipDao;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.service.user.UserLogicException.*;
import static ru.yandex.practicum.filmorate.exception.storage.user.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Service("UserDbService")
public class UserDbService implements UserService {
    private final Storage<User> userStorage;
    private final FriendshipDao friendshipDao;

    @Autowired
    public UserDbService(@Qualifier("UserDbStorage") Storage<User> userStorage,
                         FriendshipDao friendshipDao) {
        log.debug("UserDbService({}, {}).",
                userStorage.getClass().getSimpleName(),
                friendshipDao.getClass().getSimpleName());
        this.userStorage = userStorage;
        log.info(DEPENDENCY_MESSAGE, userStorage.getClass().getName());
        this.friendshipDao = friendshipDao;
        log.info(DEPENDENCY_MESSAGE, friendshipDao.getClass().getSimpleName());
    }

    @Override
    public User add(User user) {
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public User get(long userID) {
        return userStorage.get(userID);
    }

    @Override
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public void addFriend(long userID, long friendID) {
        log.debug("addFriend({}, {}).", userID, friendID);
        if (!userStorage.contains(userID)) {
            log.warn("Не удалось добавить друга: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (userID == friendID) {
            log.warn("Не удалось добавить друга: {}.", format(UNABLE_TO_ADD_YOURSELF, userID));
            throw new UserLogicException(format(UNABLE_TO_ADD_YOURSELF, userID));
        }
        if (!userStorage.contains(friendID)) {
            log.warn("Не удалось добавить друга: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }

        boolean isMutual = userStorage.get(friendID).getFriends().contains(userID);
        friendshipDao.add(friendID, userID, isMutual);
        log.trace("Добавлен друг ID_{} к пользователю ID_{}.", friendID, userID);
    }

    @Override
    public void deleteFriend(long userID, long friendID) {
        log.debug("deleteFriend({}, {}).", userID, friendID);
        if (!userStorage.contains(userID)) {
            log.warn("Не удалось удалить друга: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (userID == friendID) {
            log.warn("Не удалось удалить друга: {}.", format(UNABLE_TO_DELETE_YOURSELF, userID));
            throw new UserLogicException(format(UNABLE_TO_DELETE_YOURSELF, userID));
        }
        if (!userStorage.contains(friendID)) {
            log.warn("Не удалось удалить друга: {}.", format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, friendID));
        }
        friendshipDao.delete(friendID, userID);
        log.trace("Удалён друг ID_{} у пользователя ID_{}.", friendID, userID);
    }

    @Override
    public Collection<User> getFriends(long userID) {
        log.debug("getFriends({}).", userID);
        if (!userStorage.contains(userID)) {
            log.warn("Список друзей не вовзращён: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        List<User> friends = friendshipDao.getFromUserID(userID).stream()
                .mapToLong(Long::valueOf)
                .mapToObj(userStorage::get)
                .collect(Collectors.toList());
        log.trace("Возвращён список друзей: {}.", friends);
        return friends;
    }

    @Override
    public Collection<User> getCommonFriends(long userID, long otherUserID) {
        log.debug("getCommonFriends({}, {}).", userID, otherUserID);
        if (!userStorage.contains(userID)) {
            log.warn("Не удалось вернуть общих друзей: {}.", format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, userID));
        }
        if (userID == otherUserID) {
            log.warn("Не удалось вернуть общих друзей: {}.", format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
            throw new UserLogicException(format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
        }
        if (!userStorage.contains(otherUserID)) {
            log.warn("Не удалось вернуть общих друзей: {}.", format(USER_NOT_FOUND, otherUserID));
            throw new UserNotFoundException(format(USER_NOT_FOUND, otherUserID));
        }
        List<User> commonFriends = CollectionUtils.intersection(
                        friendshipDao.getFromUserID(userID),
                        friendshipDao.getFromUserID(otherUserID)).stream()
                .mapToLong(Long::valueOf)
                .mapToObj(userStorage::get)
                .collect(Collectors.toList());
        log.trace("Возвращён список общих друзей: {}.", commonFriends);
        return commonFriends;
    }
}