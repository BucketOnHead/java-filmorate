package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.storage.dao.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;

import java.util.Collection;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.dao.genre.GenreNotFoundException.GENRE_NOT_FOUND;

@Slf4j
@Service("GenreDbService")
public class GenreDbService implements GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreDbService(GenreDao genreDao) {
        log.debug("GenreDbService({}).", genreDao.getClass().getSimpleName());
        this.genreDao = genreDao;
        log.info(DEPENDENCY_MESSAGE, genreDao.getClass().getName());
    }

    @Override
    public Genre get(long genreID) {
        if (genreID > (int) genreID) {
            throw new IllegalArgumentException("genreID должен быть типа INTEGER");
        }
        if (!genreDao.contains((int) genreID)) {
            log.warn("Не удалось вернуть жанр: {}.", format(GENRE_NOT_FOUND, genreID));
            throw new GenreNotFoundException(format(GENRE_NOT_FOUND, genreID));
        }
        return genreDao.get((int) genreID);
    }

    @Override
    public Collection<Genre> getAll() {
        return genreDao.getAll();
    }
}
