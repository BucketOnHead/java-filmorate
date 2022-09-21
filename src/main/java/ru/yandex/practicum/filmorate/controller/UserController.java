package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        log.debug("UserController({}, {}).",
                userStorage.getClass().getSimpleName(),
                userService.getClass().getSimpleName());
        this.userStorage = userStorage;
        log.info("Подключена зависимость: {}.", userStorage.getClass().getName());
        this.userService = userService;
        log.info("Подключена зависимость: {}.", userService.getClass().getName());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userStorage.add(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addUserFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userStorage.get(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
    }
}
