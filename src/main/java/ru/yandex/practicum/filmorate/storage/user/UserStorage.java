package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    List<User> getUsers();

    void addUserFriend(long userID, long friendID);


    void deleteUserFriend(long userID, long friendID);

    List<User> getUserFriends(long userID);

    List<User> getMutualFriends(long userID, long friendID);

    User getUser(long userID);
}
