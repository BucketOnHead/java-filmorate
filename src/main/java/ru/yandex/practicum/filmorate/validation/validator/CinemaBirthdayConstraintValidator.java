package ru.yandex.practicum.filmorate.validation.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.validation.annotation.AfterCinemaBirthday;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Валидатор для аннотации {@link AfterCinemaBirthday}.
 */
public class CinemaBirthdayConstraintValidator implements ConstraintValidator<AfterCinemaBirthday, LocalDate> {
    private static final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(@NonNull LocalDate date, ConstraintValidatorContext cxt) {
        return date.isAfter(cinemaBirthday);
    }
}
