package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Component()
@Data
@Slf4j
public class InMemoryUserStorage implements UserStorage {


    private final HashMap<Long, User> users;
    private static Long id = 1L;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
    }

    public static Long getId() {
        return id++;
    }

    @Override
    public Collection<User> allUsers() {
        log.debug("Пользователей добавлено: " + users.size());
        return users.values();
    }

    @Override
    public User create(User user) throws NotFoundException {

        if (!users.containsKey(user.getId())) {
            user.setId(getId());
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("Новый пользователь не создан");
        }
        return user;
    }

    @Override
    public User update(User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.info(String.format("Пользователь с id %d обновлен", user.getId()));
            users.put(user.getId(), user);
        } else {
            log.info("Пользователя нет в базе");
            throw new NotFoundException("404");
        }
        return user;

    }

    @Override
    public User getById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
    }

}
