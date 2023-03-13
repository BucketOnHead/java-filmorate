package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.storage.dao.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.exception.storage.dao.mpa.MpaNotFoundException.MPA_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaDbService implements MpaService {
    private final MpaDao mpaDao;

    @Override
    public Mpa get(long mpaID) throws MpaNotFoundException {
        if (mpaID > (int) mpaID) {
            throw new IllegalArgumentException("mpaID должен быть типа INTEGER");
        }
        if (!mpaDao.contains((int) mpaID)) {
            log.warn("Не удалось вернуть рейтинг MPA: {}.", String.format(MPA_NOT_FOUND, mpaID));
            throw new MpaNotFoundException(String.format(MPA_NOT_FOUND, mpaID));
        }
        return mpaDao.get((int) mpaID);
    }

    @Override
    public Collection<Mpa> getAll() {
        return mpaDao.getAll();
    }
}
