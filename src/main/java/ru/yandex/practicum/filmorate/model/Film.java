package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Long id;

    @NotBlank(message = "Название фильма обязательно при создании", groups = OnCreate.class)
    @Pattern(regexp = "^\\S.*\\S$",
            message = "Название не должно содержать только пробелы", groups = OnCreate.class)
    private String name;

    @NotBlank(message = "Описание фильма обязательно при создании", groups = OnCreate.class)
    @Size(max = 200, message = "Описание не может превышать 200 символов", groups = OnCreate.class)
    private String description;

    @NotNull(message = "Дата релиза обязательна при создании", groups = OnCreate.class)
    @PastOrPresent(message = "Дата релиза не может быть в будущем", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate releaseDate;


    @NotNull(message = "Длительность обязательна при создании", groups = OnCreate.class)
    @Positive(message = "Длительность должна быть положительной", groups = {OnCreate.class, OnUpdate.class})
    private Integer duration;

    private final Set<Long> likes = new HashSet<>();
}