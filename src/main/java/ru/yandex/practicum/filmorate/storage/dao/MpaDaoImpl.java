package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaDao;

import java.util.Collection;

@Component
@Slf4j
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> allMpa() {

        return this.jdbcTemplate.query("SELECT * FROM MPA", mpaRowMapper);
    }

    @Override
    public Mpa getById(Long id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE MPA_ID = ?", id);
        if (sqlRowSet.next()) {
            Mpa mpa = new Mpa(
                    sqlRowSet.getLong("MPA_ID"),
                    sqlRowSet.getString("MPA_NAME")
            );
            log.info("Рейтинг с id {} найден", mpa.getId());
            return mpa;
        }
        throw new NotFoundException(String.format("Рейтинг с id: %d не найден", id));
    }

    private final RowMapper<Mpa> mpaRowMapper = (resultSet, rowNum) -> {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("MPA_ID"));
        mpa.setName(resultSet.getString("MPA_NAME"));
        return mpa;
    };


}
