package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping
@Slf4j
public class FilmController {
    HashMap<Long, Film> films = new HashMap<>();
    private long id = 1;
    private static final int MAX_LENGTH_DESCRIPTION = 200;
    private static final LocalDate RELEASE_DATE = LocalDate.parse("1895-12-28", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    @GetMapping("/films")
    public Collection<Film> allFilms() {
        log.debug("В кинотеке:  " + films.size() + " фильмов");
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            film.setId(id);

            if (film.getName().isEmpty()) {
                log.debug("Фильм не добавлен. Название фильма пуст");
                throw new ValidationException();
            } else if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
                log.debug("Фильм не добавлен. Длина описания более 200 символов");
                throw new ValidationException();
            } else if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
                log.debug("Фильм не добавлен. Дата релиза раньше, чем 28 декабря 1895 года");
                throw new ValidationException();
            } else if (film.getDuration() < 0) {
                log.debug("Фильм не добавлен. Продолжительность фильма должна быть положительной");
                throw new ValidationException();
            }
            films.put(film.getId(), film);
            id++;

        }
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            if (film.getName().isEmpty()) {
                log.debug("Фильм не добавлен или не обновлен. Название пустое");
                throw new ValidationException();
            } else if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
                log.debug("Фильм не добавлен или не обновлен. Длина описания более 200 символов");
                throw new ValidationException();
            } else if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
                log.debug("Фильм не добавлен или не обновлен. Дата релиза раньше, чем 28 декабря 1985 года");
                throw new ValidationException();
            } else if (film.getDuration() < 0) {
                log.debug("Фильм не добавлен или не обновлен. Длительность фильма не положительна");
                throw new ValidationException();
            } else {
                films.put(film.getId(), film);
            }
        } else {
            log.debug("Такого фильма в списке нет");
            throw new ValidationException();

        }
        return film;
    }
}

