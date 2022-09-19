package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.storage.user.UserStorageException.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long uniqueUserID = 0;

    @Override
    public User addUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new UserStorageException(
                    format(USER_ALREADY_EXISTS, user.getId()));
        }
        registerUser(user);
        return users.put(user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserStorageException(
                    format(USER_NOT_FOUND, user.getId()));
        }
        return users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserStorageException(
                    format(USER_NOT_FOUND, user.getId()));
        }
        users.remove(user.getId());
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User addUserFriend(long userID, long friendID) {
        if (!users.containsKey(userID)) {
            throw new UserStorageException(
                    format(USER_NOT_FOUND, userID));
        }

        if (userID == friendID) {
            throw new UserStorageException(
                    format(UNABLE_TO_ADD_YOURSELF, userID));
        }

        if (!users.containsKey(friendID)) {
            throw new UserStorageException(
                    format(USER_NOT_FOUND, friendID));
        }

        users.get(friendID).getFriends().add(userID);
        User user = users.get(userID);
        user.getFriends().add(friendID);
        return user;
    }

    public User deleteUserFriend(long userID, long friendID) {
        if (!users.containsKey(userID)) {
            throw new UserStorageException(
                    format(USER_NOT_FOUND, userID));
        }

        if (userID == friendID) {
            throw new UserStorageException(
                    format(UNABLE_TO_DELETE_YOURSELF, userID));
        }

        if (!users.containsKey(friendID)) {
            throw new UserStorageException(
                    format(USER_NOT_FOUND, friendID));
        }

        users.get(friendID).getFriends().remove(userID);
        User user = users.get(userID);
        user.getFriends().remove(friendID);
        return user;
    }

    public List<Long> getUserFriends(long userID) {
        if (!users.containsKey(userID)) {
            throw new UserStorageException(
                    format(USER_NOT_FOUND, userID));
        }
        return users.get(userID).getFriends();
    }

    private void registerUser(User user) {
        user.setId(uniqueUserID++);
    }
}
