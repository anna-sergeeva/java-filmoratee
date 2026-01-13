package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTests {
    private Validator validator;
    private User validUser;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        validUser = new User();
        validUser.setEmail("valid@email.com");
        validUser.setLogin("validLogin");
        validUser.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void emailIsBlankOnCreateTest() {
        User user = new User();
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при пустом email (создание)");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Электронная почта не может быть пустой")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }

    @Test
    void emailInvalidFormatOnUpdateTest() {
        User user = validUser;
        user.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при некорректном email (обновление)");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Электронная почта должна содержать символ @ и быть корректной")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }

    @Test
    void loginIsBlankOnCreateTest() {
        User user = new User();
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при пустом логине (создание)");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Логин не может быть пустым")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }

    @Test
    void loginHasSpacesOnUpdateTest() {
        User user = validUser;
        user.setLogin("login with spaces");
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при логине с пробелами (обновление)");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Логин не может содержать пробелы")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }

    @Test
    void birthdayInFutureOnUpdateTest() {
        User user = validUser;
        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при дате рождения в будущем (обновление)");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Дата рождения не может быть в будущем")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }
}