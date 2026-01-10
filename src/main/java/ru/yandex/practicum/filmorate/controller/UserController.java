package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.BuildOperations;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Validated(BuildOperations.class) @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
           user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
    // 1. Проверяем, что id указан (обязательное поле для PUT)
        if (newUser.getId() == null) {
            log.warn("Ошибка при обновлении пользователя {}: Должен быть указан id (идентификатор)", newUser);
            throw new ValidationException("Должен быть указан id (идентификатор)");
        }

        Integer id = newUser.getId();

    // 2. Проверяем существование пользователя в хранилище
        if (!users.containsKey(id)) {
            log.warn("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }

        User oldUser = users.get(id);

    // 3. Обновляем поля только если они не null в новом объекте
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }
    // 4. Если имя пустое — заменяем на логин (по бизнес-логике)
        if (oldUser.getName() != null && oldUser.getName().isBlank()) {
            oldUser.setName(oldUser.getLogin());
        }
    // 5. Логируем успешное обновление
        log.info("Данные пользователя {} обновлены", oldUser);
        return oldUser;
    }

    // вспомогательный метод для генерации идентификатора нового пользователя
    private int getNextId() {
        int currentMaxId = (int) users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}