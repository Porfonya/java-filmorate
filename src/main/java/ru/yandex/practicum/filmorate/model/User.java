package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Long id;
    private Set<Long> friends = new HashSet<>();
    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email должен быть не пустым")
    String email;

    @NotBlank(message = "Login логин должен быть не пустым")
    @Pattern(regexp = "\\S+", message = "Ошибка при вводе логина")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;


}
