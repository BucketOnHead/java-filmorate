package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.exception.service.user.UserLogicException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Service;

import java.util.Collection;

public interface UserService extends Service<User> {
    /**
     * Метод добавляет пользователю в друзей
     * друга.
     *
     * @param userID   идентификатор пользователя.
     * @param friendID идентификатор друга.
     * @exception UserLogicException в случае, если
     * пользователь добавляет сам себя в друзья.
     */
    void addFriend(long userID, long friendID);

    /**
     * Метод удаляет друга из друзей пользователя.
     *
     * @param userID   идентификатор пользователя.
     * @param friendID идентификатор друга.
     * @exception UserLogicException в случае, если
     * пользователь удаляет сам себя из друзей.
     */
    void deleteFriend(long userID, long friendID);

    /**
     * Метод возвращает коллекцию друзей пользователя.
     *
     * @param userID идентификатор пользователя.
     * @return коллекция друзей пользователя.
     */
    Collection<User> getFriends(long userID);

    /**
     * Метод возвращает коллекцию общих друзей между
     * одним пользователем и другим.
     *
     * @param userID идентификатор пользователя.
     * @param otherUserID идентификатор другого пользователя.
     * @return коллекция общих друзей между пользователями.
     * @exception UserLogicException в случае, если
     * пользователь запрашивает общих друзей между собой.
     */
    Collection<User> getCommonFriends(long userID, long otherUserID);
}
