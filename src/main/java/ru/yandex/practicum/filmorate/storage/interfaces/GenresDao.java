package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDao {
    List<Genre> allGenres();

    Genre getById(long id);

}