package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.UserControllerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import ru.yandex.practicum.filmorate.validator.exception.ValidatorException;

import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.exception.UserControllerException.USER_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.controller.exception.UserControllerException.USER_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    public User create(@RequestBody User user) {
        try {
            UserValidator.validate(user);

            if (users.containsKey(user.getId())) {
                throw new UserControllerException(
                        String.format(USER_ALREADY_EXISTS, user));
            }

            users.put(user.getId(), user);
            log.trace("Успешно добавлен пользователь: {}.", user);
        } catch (ValidatorException | UserControllerException e) {
            log.warn("Пользователь не добавлен: {}.", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            log.debug("Количество пользователей: {}.", users.size());
        }

        return user;
    }

    public User update(@RequestBody User user) {
        try {
            UserValidator.validate(user);

            if (!users.containsKey(user.getId())) {
                throw new UserControllerException(
                        String.format(USER_NOT_FOUND, user));
            }

            users.put(user.getId(), user);
            log.trace("Успешно добавлен пользователь: {}.", user);
        } catch (ValidatorException | UserControllerException e) {
            log.warn("Пользователь не добавлен: {}.", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            log.debug("Количество пользователей: {}.", users.size());
        }

        return user;
    }

    public Map<Integer, User> findAll() {
        log.trace("Возвращены все пользователи.");
        return users;
    }
}
