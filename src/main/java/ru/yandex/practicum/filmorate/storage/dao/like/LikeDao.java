package ru.yandex.practicum.filmorate.storage.dao.like;

public interface LikeDao {
    /**
     * Метод добавлет в хранилище лайк (от
     * пользователя фильму).
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     */
    void add(long filmID, long userID);

    /**
     * Метод удаляет лайк (от пользователя
     * фильму) из хранилища.
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     */
    void delete(long filmID, long userID);

    /**
     * Метод подсчитывает количество лайков
     * для фильма с указанным идентификатором.
     *
     * @param filmID идентификатор фильма.
     * @return Количество лайков.
     */
    int count(long filmID);

    /**
     * Метод проверяет содержится ли в хранилище
     * лайк (от пользователя фильму).
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     * @return Логическое значение, true - если
     * лайк содержится в хранилище, и false -
     * если нет.
     */
    boolean contains(long filmID, long userID);
}
