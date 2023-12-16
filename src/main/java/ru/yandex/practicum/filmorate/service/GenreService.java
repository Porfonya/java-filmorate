package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.GenresDaoImpl;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenresDaoImpl genresDaoImpl;

    public List<Genre> allGenres() {
        return genresDaoImpl.allGenres();
    }

    public Genre getFindGenreId(Long id) {
        return genresDaoImpl.getById(id);
    }
}
