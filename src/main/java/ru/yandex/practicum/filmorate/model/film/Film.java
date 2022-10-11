package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.annotation.AfterCinemaBirthday;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;

    @NotNull(message = "У фильма должно быть имя")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "У фильма должно быть описание")
    @Size(max = 200, message = "Описание не должно больше 200 символов")
    private String description;

    @NotNull(message = "У фильма должна быть указана дата релиза")
    @AfterCinemaBirthday
    private LocalDate releaseDate;

    @NotNull(message = "У фильма должна быть указана продолжительность")
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private int duration;

    @NotNull(message = "У фильма должен быть указан рейтинг MPA")
    private Mpa mpa;

    private Set<Genre> genres = new HashSet<>();

    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    public Set<Genre> getGenres() {
        return genres != null ? genres : new HashSet<>();
    }
}
