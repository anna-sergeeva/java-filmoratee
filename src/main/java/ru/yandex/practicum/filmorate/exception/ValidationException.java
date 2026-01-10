package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException { // нарушение бизнес-логики
    public ValidationException(String message) {
        super(message);
    }

}