package ru.yandex.practicum.filmorate.controller;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Data
class FilmControllerTest {
    private FilmController filmController = new FilmController();

    @Test
    @Builder
    void allFilms() throws ValidationException {

        Film film1 = Film.builder()
                .id(1L)
                .name("Джентельмены")
                .description("Фильм Гая Ричи")
                .releaseDate(LocalDate.now().minusYears(3))
                .duration(180L).build();

        Film film2 = Film.builder().id(2L)
                .name("Красотка")
                .description("прекрасный фильм")
                .releaseDate(LocalDate.now().minusYears(20))
                .duration(180L).build();

        filmController.createFilm(film1);
        filmController.createFilm(film2);

        Assertions.assertEquals(filmController.allFilms().size(), 2, "Фильмы не добавились");
    }

    @Test
    void createFilm() throws ValidationException {
        Film film1 = Film.builder()
                .id(1L)
                .name("Джентельмены")
                .description("Фильм Гая Ричи")
                .releaseDate(LocalDate.now().minusYears(3))
                .duration(180L).build();
        Assertions.assertEquals(filmController.createFilm(film1).getId(), 1L, "Фильм не добавился");

    }

    @Test
    void updateFilm() throws ValidationException {
        Film film1 = Film.builder()
                .id(1L)
                .name("Джентельмены")
                .description("Фильм Гая Ричи")
                .releaseDate(LocalDate.now().minusYears(3))
                .duration(180L).build();
        Assertions.assertEquals(filmController.createFilm(film1).getId(), 1L, "Фильм не добавился");
        film1.setName("Красотка");

        Assertions.assertEquals(filmController.getFilms().get(film1.getId()).getName(), "Красотка", "Данные фильма не обновились");

    }
}