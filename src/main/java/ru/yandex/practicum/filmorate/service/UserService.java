package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;

    public Collection<User> allUsers() {
        return userDbStorage.allUsers();
    }

    public User create(User user) throws ValidationException, NotFoundException {

        return userDbStorage.create(user);

    }

    public User update(User user) throws ValidationException, NotFoundException {

        return userDbStorage.update(user);

    }

    public User getUserById(Long userId) {
        return userDbStorage.getById(userId);
    }

}
