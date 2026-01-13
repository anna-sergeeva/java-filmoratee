package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final LocalDate MAX_RELEASE_DATE = LocalDate.now();


    @GetMapping
    public List<Film> findAll() {
        log.info("Запрос на получение всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        log.info("Запрос фильма по id: {}", id);
        return filmService.findById(id);
    }

    @PostMapping
    public Film create(@Validated(OnCreate.class) @RequestBody Film film,
                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .findFirst()
                    .orElse("Ошибка валидации");
            throw new ValidationException(errorMsg);
        }
        validateFilmForCreate(film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Validated(OnUpdate.class) @RequestBody Film film) {
        log.info("Запрос на обновление фильма: {}", film);
        if (film.getId() == null) {
            throw new ValidationException("ID фильма обязателен для обновления");
        }
        filmService.findById(film.getId());
        validateFilmForUpdate(film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }

    private void validateFilmForCreate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            log.warn("Некорректная дата релиза при создании: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getReleaseDate().isAfter(MAX_RELEASE_DATE)) {
            log.warn("Дата релиза в будущем при создании: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза не может быть в будущем");
        }
    }

    private void validateFilmForUpdate(Film film) {
        if (film.getReleaseDate() != null) {
            if (film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
                log.warn("Некорректная дата релиза при обновлении: {}", film.getReleaseDate());
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
            if (film.getReleaseDate().isAfter(MAX_RELEASE_DATE)) {
                log.warn("Дата релиза в будущем при обновлении: {}", film.getReleaseDate());
                throw new ValidationException("Дата релиза не может быть в будущем");
            }
        }
    }
}