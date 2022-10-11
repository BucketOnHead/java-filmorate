package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {
    /**
     * Метод добавляет объект в хранилище.
     *
     * @param element добавляемый объект.
     * @return Добавленный объект.
     */
    T add(T element);

    /**
     * Метод обновляет объект в хранилище.
     *
     * @param element обновлённый объект.
     * @return Обновлённый объект.
     */
    T update(T element);

    /**
     * Метод возвращает объект из хранилища
     * по идентификатору.
     *
     * @param elementID идентификатор объекта.
     * @return Объект, принадлежащий идентификатору.
     */
    T get(long elementID);

    /**
     * Метод возвращает коллекцию из всех
     * элементов хранилища.
     *
     * @return Коллекция, состоящия из всех
     * элементов хранилища.
     */
    Collection<T> getAll();

    /**
     * Метод проверяет содержится ли в хранилище
     * элемент с указанным индентификатором.
     *
     * @param elementID идентификатор элемента.
     * @return Логическое значение, true - если
     * элемент с указанным идентификатором содержится
     * в хранилище, и false - если нет.
     */
    boolean contains(long elementID);
}
