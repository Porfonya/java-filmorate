package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface FriendsDao {
    void addToFriends(long userId, long friendId);

    void removeFromFriend(long userId, long friendId);

    Collection<User> getAllFriends(long userId);

    Optional<Friends> findFriend(Long userId, Long friendId);

    Collection<User> getCommonFriends(Long userId, Long otherId);

}
