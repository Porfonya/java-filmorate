package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@Data
@RequestMapping("/mpa")
public class MpaController {
    public final MpaService mpaService;

    @GetMapping
    public Collection<Mpa> allGenres() {
        return mpaService.allMpa();
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable("id") Long id) {
        return mpaService.getFindMpaId(id);
    }
}
