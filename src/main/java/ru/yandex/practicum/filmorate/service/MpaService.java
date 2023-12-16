package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.MpaDaoImpl;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {


    private final MpaDaoImpl mpaDaoImpl;

    public Collection<Mpa> allMpa() {
        return mpaDaoImpl.allMpa();
    }

    public Mpa getFindMpaId(Long id) {
        return mpaDaoImpl.getById(id);
    }
}
