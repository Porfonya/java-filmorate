package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;


@RestController
@Validated
@Slf4j
@Data
@RequestMapping("/films")
public class FilmController {


    private final FilmService filmService;

    @GetMapping
    public Collection<Film> allFilms() {

        return filmService.allFilms();
    }

    @PostMapping
    public Film create(@RequestBody @Valid Film film) throws NotFoundException {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) throws NotFoundException, ValidationException {

        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film filmById(@PathVariable("filmId") Long filmId) throws NotFoundException {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addToLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        if (id <= 0 || userId <= 0) {
            throw new NotFoundException("404");
        }
        log.info("Пользователь добавил лайк");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        if (id <= 0 || userId <= 0) {
            throw new NotFoundException("404");
        }
        log.info("Пользователь удалил лайк");
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getBestFilms(@RequestParam(defaultValue = "10", required = false) int count) {

        return filmService.getBestFilms(count);
    }
}

