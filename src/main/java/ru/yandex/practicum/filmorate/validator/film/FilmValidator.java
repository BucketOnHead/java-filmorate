package ru.yandex.practicum.filmorate.validator.film;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.film.exception.*;

import java.time.LocalDate;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static ru.yandex.practicum.filmorate.validator.film.exception.DateException.INCORRECT_RELEASE_DATE;
import static ru.yandex.practicum.filmorate.validator.film.exception.DescriptionException.INCORRECT_DESCRIPTION;
import static ru.yandex.practicum.filmorate.validator.film.exception.DurationException.INCORRECT_DURATION;
import static ru.yandex.practicum.filmorate.validator.film.exception.NameException.INCORRECT_NAME;

@Slf4j
public class FilmValidator {
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public static void validate(@NonNull Film film) throws FilmValidatorException {
        log.debug("Валидация фильма: {}", film);
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        int duration = film.getDuration();

        if (name == null || name.isBlank()) {
            log.warn("Валидация фильма не пройдена: {}.", format(INCORRECT_NAME, name));
            throw new NameException(format(INCORRECT_NAME, name));
        }

        if (description.length() > 200) {
            log.warn("Валидация фильма не пройдена: {}.", format(INCORRECT_DESCRIPTION, description));
            throw new DescriptionException(format(INCORRECT_DESCRIPTION, description));
        }

        if (releaseDate.isBefore(MIN_RELEASE_DATE)) {
            log.warn("Валидация фильма не пройдена: {}.",
                    format(INCORRECT_RELEASE_DATE, releaseDate.format(ISO_DATE)));
            throw new DateException(format(INCORRECT_RELEASE_DATE, releaseDate.format(ISO_DATE)));
        }

        if (duration < 0) {
            log.warn("Валидация фильма не пройдена: {}.", format(INCORRECT_DURATION, duration));
            throw new DurationException(format(INCORRECT_DURATION, duration));
        }
        log.trace("Валидация фильма пройдена.");
    }
}
