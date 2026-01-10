package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.BuildOperations;
import ru.yandex.practicum.filmorate.validation.UpdateOperations;

import java.time.LocalDate;

@Data
public class Film {
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым", groups = {BuildOperations.class, UpdateOperations.class})
    private String name;

    @Size(max = 200, message = "Размер описания не должен превышать 200 символов", groups = {BuildOperations.class, UpdateOperations.class})
    private String description;

    @PastOrPresent(message = "Дата релиза не должна быть позже текущего дня", groups = {BuildOperations.class})
    LocalDate releaseDate;

    @Positive(message = "Длительность должна быть положительной", groups = {BuildOperations.class, UpdateOperations.class})
    Integer duration;
}
