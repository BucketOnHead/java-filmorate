package ru.yandex.practicum.filmorate.storage.dao.friendship;

import java.util.Collection;

public interface FriendshipDao {
    /**
     * Метод добавляет связь между пользователями
     * в хранилище.
     *
     * @param fromUserID идентификатор пользователя,
     *                   отправившего запрос на дружбу.
     * @param toUserID   идентификатор пользователя,
     *                   которому был отправлен запрос.
     * @param isMutual   логический параметр, определяющий
     *                   является ли дружба взаимной (дружба
     *                   считается взаимной, если пользователи
     *                   отправили заявки на дружбу друг другу).
     */
    void add(long fromUserID, long toUserID, boolean isMutual);

    /**
     * Метод удаляет связь между пользователями.
     *
     * @param fromUserID идентификатор пользователя,
     *                   отправившего запрос на дружбу.
     * @param toUserID   идентификатор пользователя,
     *                   которому был отправлен запрос.
     */
    void delete(long fromUserID, long toUserID);

    /**
     * Метод возвращает коллекцию идентификаторов
     * пользователей, которые отправили запрос
     * на дружбу указанному пользователю.
     *
     * @param toUserId идентификатор пользователя.
     * @return Коллекция идентификатор пользователей,
     * отправивших запрос на дружбу.
     */
    Collection<Long> getFromUserID(long toUserId);

    /**
     * Метод проверяет содержится ли в хранилище
     * запрос на дружбу (от пользователя фильму).
     *
     * @param fromUserID идентификатор пользователя,
     *                   отправившего запрос на дружбу.
     * @param toUserID идентификатор пользователя,
     *                 которому был отправлен запрос
     *                 на дружбу.
     * @return Логическое значение, true - если
     * запрос на дружюу содержится
     * в хранилище, и false - если нет.
     */
    boolean contains(long fromUserID, long toUserID);
}
