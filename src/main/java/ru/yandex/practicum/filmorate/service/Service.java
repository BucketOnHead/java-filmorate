package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.storage.Storage;

public interface Service<T> extends Storage<T> {
    /**
     * [Exception].getClass().getSimpleName(), [exception].getMessage().
     */
    String EXCEPTION_MESSAGE = "Отловлена ошибка: {}({}).";

    /**
     * [dependency].getClass().getName().
     */
    String DEPENDENCY_MESSAGE = "Подключена зависимость: {}.";

    @Override
    default boolean contains(long elementID) {
        throw new UnsupportedOperationException("Метод не предназначен для использования на уровне сервиса");
    }
}
