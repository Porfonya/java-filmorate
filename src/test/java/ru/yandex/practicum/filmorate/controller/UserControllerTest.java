package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

class UserControllerTest {
    private final UserController userController = new UserController();

    @Test
    void allUsers() throws ValidationException {
        User user1 = User.builder()
                .id(1L)
                .name("Porfiriy")
                .login("porf")
                .email("porf@yandex.ru")
                .birthday(LocalDate.now().minusYears(30))
                .build();
        User user2 = User.builder()
                .id(2L)
                .name("Pavel")
                .login("pasha")
                .email("pasha@inbox.ru")
                .birthday(LocalDate.now().minusYears(15))
                .build();
        userController.createUser(user1);
        userController.createUser(user2);
        Assertions.assertEquals(userController.allUsers().size(), 2, "Пользователи не добавились");
    }

    @Test
    void createUser() throws ValidationException {
        User user1 = User.builder()
                .id(1L)
                .name("Porfiriy")
                .login("porf")
                .email("porf@yandex.ru")
                .birthday(LocalDate.now().minusYears(30))
                .build();
        Assertions.assertEquals(userController.createUser(user1).getId(), 1, "Пользователь не добавился");
    }

    @Test
    void updateUser() throws ValidationException {
        User user2 = User.builder()
                .id(2L)
                .name("Pavel")
                .login("pasha")
                .email("pasha@inbox.ru")
                .birthday(LocalDate.now().minusYears(15))
                .build();
        Assertions.assertEquals(userController.createUser(user2).getId(), 1, "Пользователь не добавился");
        user2.setName("Porf");
        Assertions.assertEquals(userController.getUsers().get(user2.getId()).getName(), "Porf",
                "Данные пользователя обновились");
    }
}