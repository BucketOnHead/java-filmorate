package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private long id;               // идентификатор
    private String name;           // название
    private String description;    // описание
    private LocalDate releaseDate; // дата релиза
    private int duration;          // продолжительность фильма
    private List<Long> likes;      // списока ID пользователей, которые поставили 'Like'
}
