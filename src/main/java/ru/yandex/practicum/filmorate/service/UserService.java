package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.user.UserValidator;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        log.debug("Подключена зависимость: {}.", userStorage.getClass().getName());
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        UserValidator.validate(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        UserValidator.validate(user);
        return userStorage.updateUser(user);
    }

    public User getUser(long userID) {
        return userStorage.getUser(userID);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addUserFriend(long userID, long friendID) {
        userStorage.addUserFriend(userID, friendID);
    }

    public void deleteUserFriend(long userID, long friendID) {
        userStorage.deleteUserFriend(userID, friendID);
    }

    public List<User> getUserFriends(long userID) {
        return userStorage.getUserFriends(userID);
    }

    public List<User> getMutualFriends(long userID, long friendID) {
        return userStorage.getMutualFriends(userID, friendID);
    }
}
