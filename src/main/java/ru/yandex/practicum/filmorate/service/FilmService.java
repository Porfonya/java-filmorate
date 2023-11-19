package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
@Service
public class FilmService {

    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;


    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> allFilms() {
        return filmStorage.allFilms();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {
        return filmStorage.update(film);
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public void addLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getById(userId);
        if (film != null && user != null) {
            filmStorage.getFilmById(filmId).getLikes().add(userId);
        } else {
            log.info("Лайк не добавился к фильму");
            throw new NotFoundException("404");
        }
    }

    public void removeLike(Long filmId, Long userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getById(userId);
        if (film != null && user != null) {
            filmStorage.getFilmById(filmId).getLikes().remove(userId);
        } else {
            log.info("Лайк не удален");
            throw new NotFoundException("404");
        }
    }

    public List<Film> getBestFilms(int count) {
        return filmStorage.allFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
