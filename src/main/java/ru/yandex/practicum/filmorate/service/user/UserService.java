package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.user.UserValidator;
import ru.yandex.practicum.filmorate.validator.ValidatorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.user.UserServiceException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.service.user.UserServiceException.USER_NOT_FOUND;

@Slf4j
@Service
public class UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long uniqueID = 1;

    public User createUser(User user) {
        try {
            UserValidator.validate(user);

            if (users.containsKey(user.getId())) {
                throw new UserServiceException(
                        String.format(USER_ALREADY_EXISTS, user));
            }

            user.setId(uniqueID++);
            users.put(user.getId(), user);
            log.trace("Успешно добавлен пользователь: {}.", user);
        } catch (ValidatorException e) {
            log.warn("Пользователь не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (UserServiceException e) {
            log.warn("Пользователь не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера: " + e.getMessage(), e);
        } finally {
            log.debug("Количество пользователей: {}.", users.size());
        }

        return user;
    }

    public User updateUser(User user) {
        try {
            UserValidator.validate(user);

            if (!users.containsKey(user.getId())) {
                throw new UserServiceException(
                        String.format(USER_NOT_FOUND, user));
            }

            users.put(user.getId(), user);
            log.trace("Пользователь успешно обновлён: {}.", user);
        } catch (ValidatorException e) {
            log.warn("Не удалось обновить пользователя: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (UserServiceException e) {
            log.warn("Не удалось обновить пользователя: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера: " + e.getMessage(), e);
        } finally {
            log.debug("Количество пользователей: {}.", users.size());
        }

        return user;
    }

    public List<User> findAll() {
        log.trace("Возвращены все пользователи.");
        return new ArrayList<>(users.values());
    }
}
