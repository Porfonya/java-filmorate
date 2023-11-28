package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;


@RestController
@Validated
@Slf4j
@Data
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) throws NotFoundException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) throws NotFoundException, ValidationException {

        return userService.update(user);
    }

    @GetMapping
    public Collection<User> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) throws NotFoundException {
        return userService.getUserById(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addToFriends(@Valid @PathVariable("id") Long id,
                             @PathVariable(value = "friendId",
                                     required = false) Long friendId) throws NotFoundException {
        if (id <= 0 || friendId <= 0) {
            throw new NotFoundException("404");
        } else {
            log.info(String.format("Добавлен новый друг с id %d", id));
            userService.addToFriends(id, friendId);
        }
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFromFriends(@Valid @PathVariable(required = false) Long id,
                                  @Valid @PathVariable(required = false) Long friendId) {
        log.info(String.format("Удален друг с id %d", id));
        userService.removeFromFriends(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getFriends(@PathVariable("id") long id) {
        return userService.getAllFriends(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable("id") Long id,
                                      @PathVariable("otherId") Long otherId) throws NotFoundException {
        return userService.getCommonFriends(id, otherId);
    }
}

