package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final InMemoryUserStorage userStorage;

    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> allUsers() {
        return userStorage.allUsers();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) throws ValidationException, NotFoundException {
        return userStorage.update(user);
    }

    public User getUserById(Long userId) {
        return userStorage.getById(userId);
    }

    public void addToFriends(long userId, long friendId) throws NotFoundException {

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
        } else {

            throw new NotFoundException("404");
        }
    }

    public void removeFromFriends(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        if (user != null && friend != null) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
        } else {

            log.info(String.format("Пользователи с userId= %d  и friendId= %d не найдены", userId, friendId));
            throw new NotFoundException("404");
        }
    }

    public List<User> getAllFriends(Long userId) {
        return userStorage.getUsers().get(userId).getFriends().stream()
                .map(i -> userStorage.getUsers().get(i))
                .collect(Collectors.toList());
    }

    public Set<User> getCommonFriends(Long userId, Long otherId) {
        Set<User> commonList = new HashSet<>();
        Set<Long> friendsFirstUser = userStorage.getById(userId).getFriends();
        Set<Long> friendsSecondUser = userStorage.getById(otherId).getFriends();
        friendsSecondUser.forEach(friend -> {
            friendsFirstUser.stream()
                    .filter(commonFriend -> commonFriend.equals(friend))
                    .map(userStorage::getById)
                    .forEach(commonList::add);
        });
        return commonList;
    }
}
