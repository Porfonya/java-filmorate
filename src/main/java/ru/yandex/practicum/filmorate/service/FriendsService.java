package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendsDaoImpl;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendsService {
    private final FriendsDaoImpl friendsDaoImpl;

    public void createNewFriend(Long userId, Long friendId) {

        friendsDaoImpl.addToFriends(userId, friendId);
    }

    public Collection<User> getAllFriendsByUserId(Long id) {
        return friendsDaoImpl.getAllFriends(id);
    }


    public Optional<Friends> getFriendByUserId(Long userId, Long friendId) {
        return friendsDaoImpl.findFriend(userId, friendId);
    }

    public void deleteFriends(Long userId, Long friendId) {
        friendsDaoImpl.removeFromFriend(userId, friendId);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return friendsDaoImpl.getCommonFriends(id, otherId);
    }
}

