package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;


@RestController
@Slf4j
@Data
@RequestMapping("/genres")
public class GenreController {
    public final GenreService genreService;

    @GetMapping
    public List<Genre> allGenres() {
        return genreService.allGenres();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(genreService.getFindGenreId(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
