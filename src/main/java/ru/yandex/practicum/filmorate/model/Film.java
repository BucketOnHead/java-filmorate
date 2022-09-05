package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private int id; // идентификатор
    private String name; // название
    private String description; // описание
    LocalDate releaseDate; // дата релиза
    int duration; // продолжительность фильма
}
