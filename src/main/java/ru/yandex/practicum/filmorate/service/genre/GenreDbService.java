package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;

import java.util.Collection;

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
        return genreDao.get((int) genreID);
    }

    @Override
    public Collection<Genre> getAll() {
        return genreDao.getAll();
    }
}
