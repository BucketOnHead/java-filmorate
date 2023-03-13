package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Like {
    @NonNull
    private Long filmID;

    @NonNull
    private Long userID;
}
