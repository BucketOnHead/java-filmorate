# Filmorate

> Фильмов много — и с каждым годом становится всё больше.
> Чем их больше, тем больше разных оценок.
> Чем больше оценок, тем сложнее сделать выбор.
> Однако не время сдаваться!
> Перед вами бэкенд для сервиса, который будет
> работать с фильмами и оценками пользователей, а также
> возвращать топ фильмов, рекомендованных к просмотру.
> Теперь ни вам, ни вашим друзьям не придётся долго размышлять,
> что же посмотреть вечером.

## Валидация

Входные данные, которые приходят в запросе на добавление нового фильма 
или пользователя, должны соответствовать определённым критериям:

<details>
    <summary><h3>Для фильмов:</h3></summary>
    
* Название не может быть пустым
* Максимальная длина описания — 200 символов
* Дата релиза — не раньше 28 декабря 1895 года[^1]
* Продолжительность фильма должна быть положительной
 
[^1]: 28 декабря 1895 года считается днём рождения кино.
</details>

<details>
    <summary><h3>Для пользователей:</h3></summary>
    
* Электронная почта не может быть пустой и должна содержать символ '@'
* Логин не может быть пустым и содержать пробелы
* Имя для отображения может быть пустым — в таком случае будет использован логин
* Дата рождения не может быть в будущем
        
</details>

## Схема БД

![](https://github.com/IvanMarakanov/java-filmorate/blob/main/src/main/resources/schema.png?raw=true)

## Примеры запросов

<!-- Начало блока с примерами запросов для фильмов  -->
<details>
    <summary><h3>Для фильмов:</h3></summary>
    
* `Создание` фильма:
    
```SQL
INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_rating_id)
VALUES(?, ?, ?, ?, ?)
```

* `Обновление` фильма:
    
```SQL
UPDATE films
SET name=?,
    description=?,
    release_date=?,
    duration_in_minutes=?,
    mpa_rating_id=?
WHERE film_id=?
```
    
* `Получение` фильма `по идентификатору`:

```SQL
SELECT films.*,
       mpa_ratings.name,
       COUNT(film_likes.user_id) AS rate
FROM films
LEFT OUTER JOIN mpa_ratings ON films.mpa_rating_id=mpa_ratings.mpa_rating_id
LEFT OUTER JOIN film_likes ON films.film_id = film_likes.film_id
WHERE films.film_id=?
GROUP BY films.film_id
```   
    
* `Получение всех` фильмов:

```SQL
SELECT films.*,
       mpa_ratings.name,
       COUNT(film_likes.user_id) AS rate
FROM films
LEFT OUTER JOIN mpa_ratings ON films.mpa_rating_id=mpa_ratings.mpa_rating_id
LEFT OUTER JOIN film_likes ON films.film_id = film_likes.film_id
GROUP BY films.film_id
```
    
* `Получение популярных (по количеству лайков)` фильмов:
```SQL
SELECT films.*,
       mpa_ratings.name,
       COUNT(film_likes.user_id) AS rate
FROM films
LEFT OUTER JOIN mpa_ratings ON films.mpa_rating_id=mpa_ratings.mpa_rating_id
LEFT OUTER JOIN film_likes ON films.film_id=film_likes.film_id
GROUP BY films.film_id
ORDER BY rate DESC
LIMIT ?
```
    
* `Добавление лайка`:
```SQL
INSERT INTO film_likes (film_id, user_id)
VALUES (?, ?)
``` 
    
* `Удаление лайка`:
```SQL
DELETE
FROM film_likes
WHERE film_id=?
  AND user_id=?
```
</details>

<!-- Конец блока с примерами запросов для фильмов  -->
<!-- Начало Блока с примерами запросов для пользователей  -->

<details>
    <summary><h3>Для пользователей:</h3></summary>

* `Создание` пользователя:
   
```SQL
INSERT INTO users (email, login, name, birthday)
VALUES (?, ?, ?, ?)
```
    
* `Обновление` пользователя:
   
```SQL
UPDATE users
SET email=?,
    login=?,
    name=?,
    birthday=?
WHERE user_id=?
```
    
* `Получение` пользователя `по идентификатору`:

```SQL
SELECT *
FROM users
WHERE user_id=?
```   
    
* `Получение всех` пользователей:
    
```SQL
SELECT *
FROM users
``` 
    
* `Получение друзей` пользователя `по идентификатору`:
    
```SQL
SELECT users.*
FROM users
INNER JOIN friendships ON users.user_id=friendships.to_user_id
WHERE users.user_id=?
``` 
    
* `Добавление друга`
    
```SQL
INSERT INTO friendships (from_user_id, to_user_id, isMutual)
VALUES(?, ?, ?)
``` 
   
* `Удаление друга`
    
```SQL
DELETE
FROM friendships
WHERE from_user_id=?
  AND to_user_id=?
``` 
    
* `Получение общих друзей`
```SQL
SELECT users.*
FROM users
INNER JOIN user_friends ON users.user_id=friendships.from_user_id
WHERE friendships.from_user_id=?

INTERSECT

SELECT users.*
FROM users
INNER JOIN user_friends ON users.user_id = friendships.from_user_id
WHERE friendships.from_user_id=?
``` 
</details>

<!-- Конец блока с примерами запросов для пользователей  -->
<!-- Начало Блока с примерами запросов для жанров  -->

<details>
    <summary><h3>Для жанров:</h3></summary>
    
* `Получение` жанра `по идентификатору`:
    
```SQL
SELECT *
FROM genres
WHERE genre_id=?
``` 
    
* `Получение всех` жанров:
    
```SQL
SELECT *
FROM genres
```   
</details>

<!-- Конец блока с примерами запросов для жанров  -->
<!-- Начало Блока с примерами запросов для рейтингов MPA  -->

<details>
    <summary><h3>Для рейтингов MPA:</h3></summary>
    
* `Получение` рейтинга MPA `по идентификатору`:
    
```SQL
SELECT *
FROM mpa_ratings
WHERE mpa_rating_id=?
``` 
    
* `Получение всех` рейтингов MPA:
    
```SQL
SELECT *
FROM mpa_ratings
```   
</details>

<!-- Конец блока с примерами запросов для рейтингов MPA  -->
