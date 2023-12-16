package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;


@Data
@NoArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Length(max = 200, message = "Максимальная длина описания 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Min(value = 0, message = "Продолжительность фильма должна быть положительной")
    private Long duration;

    private Mpa mpa;

    private Set<Genre> genres;

    public Film(Long id, String name, String description, LocalDate releaseDate, Long duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;

    }
}
