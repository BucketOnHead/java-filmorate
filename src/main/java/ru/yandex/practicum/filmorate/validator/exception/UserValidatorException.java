package ru.yandex.practicum.filmorate.validator.exception;

public class UserValidatorException extends ValidatorException {
    public static final String INCORRECT_EMAIL;
    public static final String INCORRECT_LOGIN;
    public static final String INCORRECT_BIRTHDAY;

    static {
        INCORRECT_EMAIL = "Некорректная почта >%s\n"
                + "Электронная почта не может быть пустой и должна содержать символ @.";
        INCORRECT_LOGIN = "Некорректный логин >%s\n"
                + "Логин не может быть пустым и содержать пробелы.";
        INCORRECT_BIRTHDAY = "Некорректная дата рождения >%s\n"
                + "Дата рождения не может быть в будущем.";
    }

    public UserValidatorException() {
    }

    public UserValidatorException(String message) {
        super(message);
    }
}
