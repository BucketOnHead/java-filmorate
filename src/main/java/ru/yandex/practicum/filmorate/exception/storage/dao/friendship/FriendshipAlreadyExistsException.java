package ru.yandex.practicum.filmorate.exception.storage.dao.friendship;

public class FriendshipAlreadyExistsException extends RuntimeException {
    public static final String FRIENDSHIP_ALREADY_EXIST =
            "Запрос на дружбу от пользователя ID_%d к пользователю ID_%d уже существует";

    public FriendshipAlreadyExistsException(String message) {
        super(message);
    }
}
