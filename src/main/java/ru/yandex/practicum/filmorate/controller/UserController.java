package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping
@Slf4j
@Data
public class UserController {

    private HashMap<Long, User> users = new HashMap<>();
    private long id = 1;

    @GetMapping("/users")
    public Collection<User> allUsers() {
        log.debug("Пользователей добавлено: " + users.size());
        return users.values();
    }

    @PostMapping(value = "/users")
    public User createUser(@RequestBody User user) throws ValidationException {

        if (!users.containsKey(user.getId())) {
            user.setId(id);

            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.debug("Пользователь не создан. Электронная почта не может быть пустой и должна содержать символ @");
                throw new ValidationException();
            } else if (user.getLogin().isBlank()) {
                log.debug("Пользователь не создан. Электронная почта не может быть пустой и должна содержать символ @ ");
                throw new ValidationException();
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                log.debug("Пользователь не создан. Дата рождения не может быть в будущем");
                throw new ValidationException();
            }
            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            id++;
        }
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                log.debug("Пользователь не создан/не обновлен. Электронная почта не может быть пустой и должна содержать символ @");
                throw new ValidationException();
            } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                log.debug("Пользователь не создан/не обновлен. Логин не может быть пустым и содержать пробелы");
                throw new ValidationException();
            } else if (user.getBirthday().isAfter(LocalDate.now())) {
                log.debug("Пользователь не создан/не обновлен. Дата рождения не может быть в будущем");
                throw new ValidationException();
            } else {

                log.debug("Пользователь c id: " + user.getId() + " обновлен");
                users.put(user.getId(), user);

            }
        } else {
            log.debug("Такого пользователя нет");
            throw new ValidationException();
        }

        return user;
    }
}

