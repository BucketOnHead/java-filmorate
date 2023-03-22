# Filmorate

> Данный проект - это сервис для кинотеки,
> который дает возможность пользователям выбирать, 
> комментировать и оценивать любимые фильмы, 
> а также искать наиболее популярные среди них. 
> Кроме того, на сервисе можно добавлять друзей 
> и получать рекомендации на основе их лайков.

## Оглавление

- [База данных](#база-данных)
- [Валидация](#валидация)
- [Инструкция по установке](#инструкция-по-установке)
- [Технологический стек](#технологический-стек)

## База данных

- [Схема БД](#схема-бд)
- [Примеры запросов](#примеры-запросов)

### Схема БД

![](https://github.com/IvanMarakanov/java-filmorate/blob/main/src/main/resources/schema.png?raw=true)

### Примеры запросов

<details>
    <summary><h3>Для фильмов:</h3></summary>

* `Создание` фильма:

```SQL
INSERT INTO films (name,
                   description,
                   release_date,
                   duration_in_minutes,
                   mpa_rating_id)
VALUES (?, ?, ?, ?, ?);
```

* `Обновление` фильма:

```SQL
UPDATE
    films
SET name                = ?,
    description         = ?,
    release_date        = ?,
    duration_in_minutes = ?,
    mpa_rating_id       = ?
WHERE film_id = ?;
```

* `Получение` фильма `по идентификатору`:

```SQL
SELECT f.film_id,
       f.name,
       f.description,
       f.release_date,
       f.duration_in_minutes,
       mp.name AS mpa_rating,
       g.name  AS genre
FROM films f
         JOIN mpa_ratings mp ON f.mpa_rating_id = mp.mpa_rating_id
         JOIN film_genres fg ON f.film_id = fg.film_id
         JOIN genres g ON fg.genre_id = g.genre_id
WHERE f.film_id = ?;
```   

* `Получение всех` фильмов:

```SQL
SELECT f.film_id,
       f.name,
       f.description,
       f.release_date,
       f.duration_in_minutes,
       mp.name              AS mpa_rating,
       GROUP_CONCAT(g.name) AS genres
FROM films f
         JOIN mpa_ratings mp ON f.mpa_rating_id = mp.mpa_rating_id
         JOIN film_genres fg ON f.film_id = fg.film_id
         JOIN genres g ON fg.genre_id = g.genre_id
GROUP BY f.film_id;
```

* `Получение топ-N (по количеству лайков)` фильмов:
```SQL
SELECT f.film_id,
       f.name,
       f.description,
       f.release_date,
       f.duration_in_minutes,
       mp.name           AS mpa_rating,
       g.name            AS genre,
       COUNT(fl.user_id) AS like_count
FROM films f
         JOIN mpa_ratings mp ON f.mpa_rating_id = mp.mpa_rating_id
         JOIN film_genres fg ON f.film_id = fg.film_id
         JOIN genres g ON fg.genre_id = g.genre_id
         LEFT JOIN film_likes fl ON f.film_id = fl.film_id
GROUP BY f.film_id,
         mp.name,
         g.name
ORDER BY like_count DESC LIMIT ?;
```
</details>

<details>
    <summary><h3>Для пользователей:</h3></summary>

* `Создание` пользователя:

```SQL
INSERT INTO users (email,
                   login,
                   name,
                   birthday)
VALUES (?, ?, ?, ?)
```

* `Обновление` пользователя:

```SQL
UPDATE
    users
SET email    = ?,
    login    = ?,
    name     = ?,
    birthday = ?
WHERE user_id = ?
```

* `Получение` пользователя `по идентификатору`:

```SQL
SELECT *
FROM users
WHERE user_id = ?
```   

* `Получение всех` пользователей:

```SQL
SELECT *
FROM users
``` 

</details>

<details>
    <summary><h3>Для жанров:</h3></summary>

* `Получение` жанра `по идентификатору`:

```SQL
SELECT *
FROM genres
WHERE genre_id = ?
``` 

* `Получение всех` жанров:

```SQL
SELECT *
FROM genres
```   
</details>

<details>
    <summary><h3>Для рейтингов MPA:</h3></summary>

* `Получение` рейтинга MPA `по идентификатору`:

```SQL
SELECT *
FROM mpa_ratings
WHERE mpa_rating_id = ?
``` 

* `Получение всех` рейтингов MPA:

```SQL
SELECT *
FROM mpa_ratings
```   
</details>

## Валидация

Входные данные, поступающие в запросе,
должны соответствовать определенным критериям:

<details>
    <summary><h3>Для фильмов:</h3></summary>

* Название фильма должно быть указано и не может быть пустым
* Максимальная длина описания фильма не должна превышать 200 символов
* Дата релиза фильма должна быть не раньше 28 декабря 1895 года[^1]
* Продолжительность фильма должна быть положительной
* Рейтинг фильма должен быть указан

</details>

<details>
    <summary><h3>Для пользователей:</h3></summary>

* Электронная почта пользователя должна быть указана и соответствовать формату email
* Логин пользователя должен быть указан и не содержать пробелов
* Дата рождения пользователя должна быть указана и не может быть в будущем

</details>

## Инструкция по установке

- [Требования](#требования)
- [Установка](#установка)
- [Запуск](#запуск)

### Требования

- Apache Maven 3.6.0 или выше
- Git
- JDK 11 или выше

### Установка

1. Склонируйте репозиторий на свой компьютер с помощью команды:
```bash
git clone https://github.com/BucketOnHead/java-filmorate.git
```

2. Перейдите в директорию проекта:
```bash
cd java-filmorate
```

3. Соберите проект с помощью Apache Maven:
```bash
mvn clean install
```

### Запуск

После установки проекта, вы можете запустить его с помощью команды:
```bash
mvn spring-boot:run
```

## Технологический стек

- Java 11
- Spring boot 2
- JDBC, SQL, H2
- Apache Maven

[^1]: 28 декабря 1895 года считается днём рождения кино.
