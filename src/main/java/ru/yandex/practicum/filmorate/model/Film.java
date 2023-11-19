package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть путым")
    @Length(max = 200, message = "Максимальная длина описания 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private Long duration;

    private final Set<Long> likes = new HashSet<>();

}
