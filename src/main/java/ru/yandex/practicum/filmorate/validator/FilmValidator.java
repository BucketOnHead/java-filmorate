package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.exception.FilmValidatorException;

import java.time.Duration;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static ru.yandex.practicum.filmorate.validator.exception.FilmValidatorException.*;

public class FilmValidator {
    public static void validate(@NonNull Film film) throws FilmValidatorException {
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Duration duration = film.getDuration();

        if (name == null || name.isBlank()) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_NAME, name));
        }

        if (description.length() > 200) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_DESCRIPTION, description));
        }

        if (releaseDate.isBefore(Film.MIN_RELEASE_DATE)) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_RELEASE_DATE, releaseDate.format(ISO_DATE)));
        }

        if (duration.isNegative()) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_DURATION, duration));
        }
    }
}
