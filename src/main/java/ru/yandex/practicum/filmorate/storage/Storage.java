package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Map;

public interface Storage<T> {
    /**
     * Метод добавляет объект T в хранилище
     *
     * @param element добавляемый объект
     * @return добавленный объект T
     */
    T add(T element);

    /**
     * Метод обновляет объект T в хранилище
     *
     * @param element обновлённый объект
     * @return обновлённый объект T
     */
    T update(T element);

    /**
     * Метод возвращает объект T из хранилища
     * по идентификатору
     *
     * @param elementID идентификатор объекта
     * @return объект T, принадлежащий elementID
     */
    T get(long elementID);

    /**
     * Метод возвращает коллекцию из всех
     * элементов хранилища
     *
     * @return коллекция, состоящия из всех
     * элементов хранилища
     */
    Collection<T> getAll();

    /**
     * Метод возвращает внутренне хранилище
     *
     * @return внутрение хранилище
     */
    Map<Long, T> getStorage();
}
