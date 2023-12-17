package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.LikesDaoImpl;


import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesDaoImpl likesDao;

    public void addLike(Long filmId, Long userId) {
        likesDao.like(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        likesDao.dislike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return likesDao.getPopular(count);
    }

}
