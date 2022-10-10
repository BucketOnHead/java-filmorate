package ru.yandex.practicum.filmorate.service.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.storage.dao.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;

import java.util.Collection;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.storage.dao.mpa.MpaNotFoundException.*;

@Slf4j
@Service("MpaDbService")
public class MpaDbService implements MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaDbService(MpaDao mpaDao) {
        log.debug("MpaDbService({}).", mpaDao.getClass().getSimpleName());
        this.mpaDao = mpaDao;
        log.info(DEPENDENCY_MESSAGE, mpaDao.getClass().getName());
    }

    @Override
    public Mpa get(long mpaID) throws MpaNotFoundException {
        if (mpaID > (int) mpaID) {
            throw new IllegalArgumentException("mpaID должен быть типа INTEGER");
        }
        if (!mpaDao.contains((int) mpaID)) {
            log.warn("Не удалось вернуть рейтинг MPA: {}.", format(MPA_NOT_FOUND, mpaID));
            throw new MpaNotFoundException(format(MPA_NOT_FOUND, mpaID));
        }
        return mpaDao.get((int) mpaID);
    }

    @Override
    public Collection<Mpa> getAll() {
        return mpaDao.getAll();
    }
}
