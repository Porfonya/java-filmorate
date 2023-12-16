package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Component()
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public static final LocalDate RELEASE_DATE =
            LocalDate.parse("1895-12-28", DateTimeFormatter.ofPattern("yyyy-MM-dd"));


    @Override
    public Collection<Film> allFilms() {
        Collection<Film> value = this.jdbcTemplate.query("SELECT * FROM FILMS " +
                "JOIN MPA M on M.MPA_ID = FILMS.MPA_ID", filmRowMapper());
        for (Film film : value) {
            film.setGenres(getGenres(film.getId()));
        }
        return value;
    }

    @Override
    public Film create(Film film) {

        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.info("Фильм не добавлен. Дата релиза раньше, чем 28 декабря 1895 года");
            throw new ValidationException("400");
        }
        String sqlQuery = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID ) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;

        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        film.setId(id);

        if (film.getGenres() != null) {
            addGenres(film);
        }

        return film;
    }

    @Override
    public Film update(Film film) {

        String sqlQuery = "UPDATE FILMS SET FILM_NAME = ?,DESCRIPTION = ?, RELEASE_DATE = ?, DURATION =?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";

        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) > 0) {

            if (film.getGenres() != null) {

                updateGenres(film);
                film.setGenres(getGenres(film.getId()));
            }
            return film;
        } else {
            throw new NotFoundException("404");
        }
    }

    @Override
    public Film getFilmById(Long filmId) {

        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS " +
                "JOIN MPA M on M.MPA_ID = FILMS.MPA_ID " +
                "WHERE FILMS.FILM_ID = ?", filmId);
        if (filmRow.next()) {
            Film film = new Film(
                    filmRow.getLong("FILM_ID"),
                    filmRow.getString("FILM_NAME"),
                    filmRow.getString("DESCRIPTION"),
                    Objects.requireNonNull(filmRow.getDate("RELEASE_DATE")).toLocalDate(),
                    filmRow.getLong("DURATION"),
                    new Mpa(filmRow.getLong("MPA_ID"), filmRow.getString("MPA_NAME"))

            );
            if (getGenres(film.getId()) != null) {
                film.setGenres(getGenres(film.getId()));
            }

            log.info("Найден фильм с id {} ", film.getId());

            return film;

        }
        throw new NotFoundException(String.format("Фильм с id %d  не найден", filmId));

    }


    @Override
    public void addGenres(Film film) {

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO FILMS_GENRES (FILMS_ID, GENRE_ID) VALUES ( ?, ?)",
                    film.getId(), genre.getId());
            log.info("Жанры добавлены в фильме с id: {}", film.getId());
        }
    }

    @Override
    public void updateGenres(Film film) {
        deleteGenres(film);
        addGenres(film);
    }

    @Override
    public void deleteGenres(Film film) {
        jdbcTemplate.update("DELETE FROM FILMS_GENRES WHERE FILMS_ID = ?", film.getId());
        log.info("Все жанры удалены у фильма с  id {}", film.getId());
    }

    @Override
    public Set<Genre> getGenres(Long filmId) {
        return new HashSet<>
                (jdbcTemplate.query(
                        "SELECT f.GENRE_ID, g.GENRE_NAME FROM FILMS_GENRES AS f " +
                                "JOIN GENRES AS g ON f.GENRE_ID = g.GENRE_ID " +
                                "WHERE f.FILMS_ID = ? ORDER BY g.GENRE_ID",
                        new GenreMapper(), filmId));

    }

    private RowMapper<Film> filmRowMapper() {
        return (ResultSet rs, int rowNum) -> new Film(
                rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
                new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME"))
        );

    }

    public static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre();
            genre.setId(rs.getLong("GENRE_ID"));
            genre.setName(rs.getString("GENRE_NAME"));
            return genre;
        }
    }
}
