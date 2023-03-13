package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.storage.Storage;

public interface Service<T> extends Storage<T> {

    @Override
    default boolean contains(long elementID) {
        throw new UnsupportedOperationException("Метод не предназначен для использования на уровне сервиса");
    }
}
