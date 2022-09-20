package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;                           // идентификатор
    private String name;                       // название
    private String description;                // описание
    private LocalDate releaseDate;             // дата релиза
    private int duration;                      // продолжительность фильма
    private Set<Long> likes = new HashSet<>(); // список ID пользователей, которые поставили 'Like'
}
