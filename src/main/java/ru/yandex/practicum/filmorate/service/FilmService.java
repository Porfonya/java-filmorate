package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {


    private final FilmDbStorage filmDbStorage;

    public Collection<Film> allFilms() {
        return filmDbStorage.allFilms();
    }

    public Film create(Film film) {
        return filmDbStorage.create(film);
    }

    public Film update(Film film) throws ValidationException {

        return filmDbStorage.update(film);
    }

    public Film getFilmById(Long filmId) {
        return filmDbStorage.getFilmById(filmId);
    }

}