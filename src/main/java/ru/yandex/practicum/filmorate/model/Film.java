package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Размер описания не должен превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза обязательна")
    //@Positive(message = "Продолжительность должна быть положительным числом")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность должна быть указана")
    @Positive(message = "Продолжительность должна быть положительной")
    private Integer duration;

    private final Set<Long> likes = new HashSet<>();
}