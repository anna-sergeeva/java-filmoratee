package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FilmValidationTests {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void nameIsBlankTest() {
        Film film = new Film();
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при пустом названии");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Название фильма обязательно при создании")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }

    @Test
    void nameHasOnlySpacesTest() {
        Film film = new Film();
        film.setName(" ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации, если название состоит только из пробелов");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Название не должно содержать только пробелы")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }

    @Test
    void descriptionLengthMax200Test() {
        Film film = new Film();
        String longDescription = "a".repeat(201);
        film.setDescription(longDescription);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при описании >200 символов");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Описание не может превышать 200 символов")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }

    @Test
    void durationIsNegativeTest() {
        Film film = new Film();
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Длительность должна быть положительной")));
    }

    @Test
    void durationIsZeroTest() {
        Film film = new Film();
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Длительность должна быть положительной")));
    }

    @Test
    void releaseDateIsNullTest() {
        Film film = new Film();
        film.setReleaseDate(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
        assertFalse(violations.isEmpty(), "Должны быть ошибки валидации при отсутствии даты релиза");
        assertTrue(violations.stream()
                        .anyMatch(v -> v.getMessage().equals("Дата релиза обязательна при создании")),
                "Сообщение об ошибке должно точно соответствовать ожидаемому");
    }
}