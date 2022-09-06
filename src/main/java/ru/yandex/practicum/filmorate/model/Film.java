package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private long id; // идентификатор
    private String name; // название
    private String description; // описание
    LocalDate releaseDate; // дата релиза
    int duration; // продолжительность фильма
}
