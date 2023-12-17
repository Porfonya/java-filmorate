package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void create() {
        Film newFilm = new Film(1L, "Very good Film", "Best", LocalDate.of(1989, 3, 4), 123L, new Mpa(1L, "G"));
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        newFilm.setGenres(filmDbStorage.getGenres(newFilm.getId()));
        assertThat(newFilm)
                .isNotNull();

    }

    @Test
    public void update() {
        Film oldFilm = new Film(1L, "Very good Film", "Best", LocalDate.of(1989, 3, 4), 123L, new Mpa(1L, "G"));
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.create(oldFilm);
        oldFilm.setGenres(filmDbStorage.getGenres(oldFilm.getId()));

        Film newFilm = new Film(1L, "Very bad Film", "Bad", LocalDate.of(1989, 3, 4), 123L, new Mpa(1L, "G"));

        filmDbStorage.update(newFilm);
        newFilm.setGenres(filmDbStorage.getGenres(newFilm.getId()));
        Film updateFilm = filmDbStorage.getFilmById(1L);

        assertThat(updateFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void getFilmById() {
        Film newFilm = new Film(1L, "Very good Film", "Best", LocalDate.of(1989, 3, 4), 123L, new Mpa(1L, "G"));
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.create(newFilm);

        newFilm.setGenres(filmDbStorage.getGenres(newFilm.getId()));
        Film saveFilm = filmDbStorage.getFilmById(1L);

        assertThat(saveFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }
}