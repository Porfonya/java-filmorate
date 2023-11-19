package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();
    private Long id = 1L;
    public static final LocalDate RELEASE_DATE =
            LocalDate.parse("1895-12-28", DateTimeFormatter.ofPattern("yyyy-MM-dd"));


    public Long getId() {
        return id++;
    }

    public Collection<Film> allFilms() {
        log.info("В кинотеке:  " + films.size() + " фильмов");
        return films.values();
    }

    @Override
    public Film create(Film film) throws ValidationException {

        if (!films.containsKey(film.getId())) {

            if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
                log.debug("Фильм не добавлен. Дата релиза раньше, чем 28 декабря 1895 года");
                throw new ValidationException("400");
            }
            film.setId(getId());
            films.put(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            log.info("Фильм отсутствует в базе");
            throw new NotFoundException("404");
        }

    }

    @Override
    public Film getFilmById(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else throw new NotFoundException("404");

    }

}
