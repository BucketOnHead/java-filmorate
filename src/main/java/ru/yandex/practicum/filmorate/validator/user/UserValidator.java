package ru.yandex.practicum.filmorate.validator.user;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.user.exception.BirthdayException;
import ru.yandex.practicum.filmorate.validator.user.exception.EmailException;
import ru.yandex.practicum.filmorate.validator.user.exception.LoginException;

import java.time.LocalDate;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static ru.yandex.practicum.filmorate.validator.user.exception.BirthdayException.INCORRECT_BIRTHDAY;
import static ru.yandex.practicum.filmorate.validator.user.exception.EmailException.INCORRECT_EMAIL;
import static ru.yandex.practicum.filmorate.validator.user.exception.LoginException.INCORRECT_LOGIN;

@Slf4j
public class UserValidator {
    public static void validate(@NonNull User user) {
        log.debug("Валидация пользователя: {}", user);
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthDay = user.getBirthday();

        if (email == null || !email.contains("@")) {
            log.warn("Валидация пользователя не пройдена: {}.", format(INCORRECT_EMAIL, email));
            throw new EmailException(format(INCORRECT_EMAIL, email));
        }

        if (login == null || login.indexOf(' ') >= 0) {
            log.warn("Валидация пользователя не пройдена: {}.", format(INCORRECT_LOGIN, login));
            throw new LoginException(format(INCORRECT_LOGIN, login));
        }

        if (birthDay.isAfter(LocalDate.now())) {
            log.warn("Валидация пользователя не пройдена: {}.",
                    format(INCORRECT_BIRTHDAY, birthDay.format(ISO_DATE)));
            throw new BirthdayException(format(INCORRECT_BIRTHDAY, birthDay.format(ISO_DATE)));
        }

        if (name == null || name.isBlank()) {
            user.setName(login);
            log.debug("Пользователю присвоенно имя {}: {}", login, user);
        }

        log.trace("Валидация пользователя пройдена.");
    }
}
