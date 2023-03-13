package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private Long id;

    @NotNull(message = "У пользователя должна быть указанна эл.почта")
    @Email(message = "Некорректная почта")
    private String email;

    @NotNull(message = "У пользователя должен быть указан логин")
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "\\S+", message = "В логине не могут находиться пробелы")
    private String login;

    private String name;

    @NotNull(message = "У пользователя должна быть указанна дата рождения")
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public String getName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
        return name;
    }
}
