# java-filmorate

> Фильмов много — и с каждым годом становится всё больше.
> Чем их больше, тем больше разных оценок.
> Чем больше оценок, тем сложнее сделать выбор.
> Однако не время сдаваться!
> Перед вами бэкенд для сервиса, который будет 
> работать с фильмами и оценками пользователей, а также
> возвращать топ фильмов, рекомендованных к просмотру.
> Теперь ни вам, ни вашим друзьям не придётся долго размышлять,
> что же посмотреть вечером.

## Схема БД
<a href="https://drawsql.app/teams/my-team-172/diagrams/java-filmorate" title="перейти на сайт drawsql.app">
    <img src="https://github.com/IvanMarakanov/java-filmorate/blob/main/src/main/resources/diagram.png" alt="Нажмите, чтобы увидеть диаграмму" />
</a>

## Примеры запросов
<details>
    <summary><h3>Для фильмов:</h3></summary>
    
* Получение списка всех фильмов:
```SQL
SELECT *
FROM films;
```
* Получение информации по фильму по его id:
```SQL
SELECT *
FROM films
WHERE films.film_id = <?>; -- id фильма
```   
</details>

<details>
    <summary><h3>Для пользователей:</h3></summary>

* Получение списка всех пользователей:
```SQL
SELECT *
FROM users;
```

* Получение информации по пользователю по его id:
```SQL
SELECT *
FROM users
WHERE users.user_id = <?>; -- id пользователя
```   
</details>
