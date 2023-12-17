package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenresDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class GenresDaoImpl implements GenresDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> allGenres() {

        return jdbcTemplate.query("SELECT * FROM GENRES", new GenreMapper());
    }

    @Override
    public Genre getById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM GENRES " +
                "WHERE GENRE_ID = ?", genreRowMapper(), id);
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

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getLong("GENRE_ID"),
                rs.getString("GENRE_NAME"));

    }
}
