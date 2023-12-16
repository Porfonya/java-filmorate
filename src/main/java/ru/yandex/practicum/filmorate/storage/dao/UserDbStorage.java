package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component()
@RequiredArgsConstructor()

public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<User> allUsers() {
        String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS";
        return this.jdbcTemplate.query(sqlQuery, userRowMapper());
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into USERS(EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return user;

    }

    @Override
    public User update(User user) {

        String sqlQuery = "UPDATE USERS SET EMAIL = ?,  LOGIN = ?, USER_NAME = ?,  BIRTHDAY = ?  " +
                "WHERE USER_ID = ?";

        if (jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()) > 0) {
            return user;
        } else {
            throw new NotFoundException("404");
        }

    }

    @Override
    public User getById(long id) {

        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USER_ID = ?", userRowMapper(), id);

    }

    public RowMapper<User> userRowMapper() {
        return (rs, rowNum) ->

                new User(
                        rs.getLong("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("LOGIN"),
                        rs.getString("USER_NAME"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                );

    }

}