package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesDao;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikesDaoImpl implements LikesDao {


    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;


    @Override
    public void like(Long filmId, Long userId) {

        jdbcTemplate.update("INSERT INTO LIKES (FILMS_ID, USER_ID) VALUES ( ?, ? ) ", filmId, userId);

    }

    public Collection<Like> allLikes() {
        String sqlQuery = "SELECT * FROM LIKES";
        return this.jdbcTemplate.query(sqlQuery, likesRowMapper());
    }

    @Override
    public void dislike(Long filmId, Long userId) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE FILMS_ID = ? AND USER_ID = ?", filmId, userId);
    }

    public List<Film> getPopular(int count) {
        List<Film> value = jdbcTemplate.query("SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, " +
                "M.MPA_ID, M.MPA_NAME " +
                "FROM FILMS F LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID LEFT JOIN LIKES L on F.FILM_ID = L.FILMS_ID " +
                "GROUP BY F.FILM_ID ORDER BY F.FILM_ID DESC LIMIT ?; ", filmRowMapper(), count);
        for (Film film : value) {
            film.setGenres(filmDbStorage.getGenres(film.getId()));
        }
        return value;

    }

    private final RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> new Film(
                rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getLong("DURATION"),
                new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")));
    }

    private final RowMapper<Like> likesRowMapper() {
        return (rs, rowNum) -> new Like(
                rs.getLong("FILMS_ID"),
                rs.getLong("USER_ID"));

    }
}
