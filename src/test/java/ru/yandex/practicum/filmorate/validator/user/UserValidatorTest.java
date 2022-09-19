package ru.yandex.practicum.filmorate.validator.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.validator.user.UserValidator.validate;

class UserValidatorTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(100L);
        user.setEmail("example@example.com");
        user.setLogin("qwerty123");
        user.setName("Zxczxc");
        user.setBirthday(LocalDate.parse("1997-01-01"));
    }

    /**
     * Method under test: {@link UserValidator#validate(User)}
     */
    @Test
    void testValidate() {
        assertDoesNotThrow(() -> validate(user));
    }

    /**
     * Method under test: {@link UserValidator#validate(User)}
     */
    @Test
    void testValidateEmail() {
        user.setEmail(null);
        assertThrows(UserValidatorException.class, () -> validate(user));
        user.setEmail("exampleexample.com");
        assertThrows(UserValidatorException.class, () -> validate(user));
    }

    /**
     * Method under test: {@link UserValidator#validate(User)}
     */
    @Test
    void testValidateLogin() {
        user.setLogin(null);
        assertThrows(UserValidatorException.class, () -> validate(user));
        user.setLogin("Best user");
        assertThrows(UserValidatorException.class, () -> validate(user));
    }

    /**
     * Method under test: {@link UserValidator#validate(User)}
     */
    @Test
    void testValidateName() {
        user.setName(null);
        assertDoesNotThrow(() -> validate(user));
        assertEquals(user.getLogin(), user.getName());
        user.setName("      ");
        assertDoesNotThrow(() -> validate(user));
        assertEquals(user.getLogin(), user.getName());
    }

    /**
     * Method under test: {@link UserValidator#validate(User)}
     */
    @Test
    void testValidateBirthday() {
        user.setBirthday(LocalDate.MAX);
        assertThrows(UserValidatorException.class, () -> validate(user));
    }
}