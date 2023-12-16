package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsDao;

import java.util.*;

@Component
@Slf4j
@AllArgsConstructor
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addToFriends(long userId, long friendId) throws NotFoundException {
        jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID)  VALUES (?, ?)", userId, friendId);
    }


    @Override
    public void removeFromFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public Collection<User> getAllFriends(long userId) {
        try {
            String sqlQuery = "SELECT U.USER_ID,U.EMAIL,U.LOGIN,U.USER_NAME,U.BIRTHDAY" +
                    " FROM FRIENDS JOIN USERS U on U.USER_ID = FRIENDS.FRIEND_ID " +
                    "WHERE FRIENDS.USER_ID = ?";

            return this.jdbcTemplate.query(sqlQuery, friendRowMapper(), userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("404");
        }
    }

    @Override
    public Optional<Friends> findFriend(Long userId, Long friendId) {
        String sqlQuery = "SELECT * FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, friendsRowMapper(), userId, friendId));
    }


    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        try {
            String sqlQuery = "SELECT U.USER_ID,U.EMAIL,U.LOGIN,U.USER_NAME,U.BIRTHDAY" +
                    " FROM FRIENDS F JOIN USERS U on U.USER_ID = F.FRIEND_ID WHERE F.USER_ID IN(?,?)" +
                    "GROUP BY U.USER_ID HAVING count(U.USER_ID)= 2";

            return this.jdbcTemplate.query(sqlQuery, friendRowMapper(), userId, otherId);
        } catch (NotFoundException e) {
            throw new NotFoundException("404");
        }
    }

    private RowMapper<Friends> friendsRowMapper() {
        return (rs, rowNum) -> new Friends(
                rs.getLong("USER_ID"),
                rs.getLong("FRIEND_ID"),
                rs.getBoolean("FRIEND_STATUS")
        );

    }

    private RowMapper<User> friendRowMapper() {
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
