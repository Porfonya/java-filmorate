package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Set;

public interface FilmStorage {
    Collection<Film> allFilms();

    Film create(Film film);

    Film update(Film film);

    Film getFilmById(Long id);

    void addGenres(Film film);

    void deleteGenres(Film film);

    void updateGenres(Film film);

    Set<Genre> getGenres(Long filmId);

}
