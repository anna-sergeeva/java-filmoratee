package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.BuildOperations;
import ru.yandex.practicum.filmorate.validation.UpdateOperations;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    @NotBlank(message = "Электронная почта не может быть пустой", groups = {BuildOperations.class})
    @Email(message = "Электронная почта должна содержать символ @ и быть корректной", groups = {BuildOperations.class, UpdateOperations.class})
    private String email;

    @NotBlank(message = "Логин не может быть пустым", groups = {BuildOperations.class})
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы", groups = {BuildOperations.class, UpdateOperations.class})
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем", groups = {BuildOperations.class, UpdateOperations.class})
    private LocalDate birthday;
}