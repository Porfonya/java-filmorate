package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;


@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendsService friendsService;

    @PostMapping
    public User create(@RequestBody @Valid User user) throws NotFoundException {
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) throws NotFoundException {
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> allUsers() {
        return userService.allUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable("id") @Valid Long id,
                             @PathVariable("friendId") @Valid Long friendId) {
        try {
            friendsService.createNewFriend(id, friendId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Данные введены неправильно");
        }
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void removeFromFriends(@Valid @PathVariable(required = false) Long id,
                                  @Valid @PathVariable(required = false) Long friendId) {
        log.info(String.format("Удален друг с id %d", id));
        friendsService.deleteFriends(id, friendId);
    }

    @GetMapping(value = "/{id}/friends")
    public Collection<User> getFriends(@PathVariable("id") long id) {
        try {
            return friendsService.getAllFriendsByUserId(id);
        } catch (Exception e) {
            throw new NotFoundException("404");
        }

    }

    @GetMapping(value = "/{id}/friends/{friendId}")
    public Optional<Friends> getFriends(@PathVariable("id") long id,
                                        @PathVariable("friendId") long friendId) {
        return friendsService.getFriendByUserId(id, friendId);

    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") Long id,
                                             @PathVariable("otherId") Long otherId) throws NotFoundException {
        return friendsService.getCommonFriends(id, otherId);
    }

}

