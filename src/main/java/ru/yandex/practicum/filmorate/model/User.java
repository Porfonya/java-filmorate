package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Min(1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email должен быть валидным")
    @NotBlank(message = "Email должен быть не пустым")
    private String email;

    @NotBlank(message = "Login логин должен быть не пустым")
    @Pattern(regexp = "\\S+", message = "Ошибка при вводе логина")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

}
