package ru.yandex.practicum.filmorate.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private long id;

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

    @JsonIgnore
    @Builder.Default
    private Set<Long> friends = new HashSet<>();

    public String getName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
        return name;
    }
}
