package ru.yandex.practicum.filmorate.storage.interfaces;


public interface LikesDao {
    void like(Long filmId, Long userId);

    void dislike(Long filmId, Long userId);
}
