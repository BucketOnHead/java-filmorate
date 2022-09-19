package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validator.ValidatorException;
import ru.yandex.practicum.filmorate.validator.user.UserValidator;

import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        try {
            UserValidator.validate(user);
            userStorage.addUser(user);
            log.trace("Успешно добавлен пользователь: {}.", user);
        } catch (ValidatorException e) {
            log.warn("Пользователь не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        }

        return user;
    }

    public User updateUser(User user) {
        try {
            UserValidator.validate(user);
            userStorage.updateUser(user);
            log.trace("Пользователь успешно обновлён: {}.", user);
        } catch (ValidatorException e) {
            log.warn("Не удалось обновить пользователя: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        }

        return user;
    }

    public List<User> findAll() {
        return userStorage.getUsers();
    }
}
