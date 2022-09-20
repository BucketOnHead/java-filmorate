package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;                             // идентификатор
    private String email;                        // электронная почта
    private String login;                        // логин пользователя
    private String name;                         // имя для отображения
    private LocalDate birthday;                  // дата рождения
    private Set<Long> friends = new HashSet<>(); // список ID друзей
}
