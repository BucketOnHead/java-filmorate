package ru.yandex.practicum.filmorate.model.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
    @NonNull
    private Long fromUserId;
    @NonNull
    private Long toUserId;
    @NonNull
    private Boolean isMutual;
}
