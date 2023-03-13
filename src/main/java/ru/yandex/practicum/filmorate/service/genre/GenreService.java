package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.Service;

public interface GenreService extends Service<Genre> {

    @Override
    default Genre add(Genre element) {
        throw new UnsupportedOperationException("Используйте data.sql чтобы добавить новые жанры");
    }

    @Override
    default Genre update(Genre element) {
        throw new UnsupportedOperationException("Используйте data.sql чтобы обновить жанр");
    }
}
