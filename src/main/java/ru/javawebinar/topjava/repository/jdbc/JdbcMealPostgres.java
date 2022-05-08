package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
@Profile("jdbc & postgres")
public class JdbcMealPostgres extends JdbcMealRepositoryAbstract<LocalDateTime> {

    @Override
    protected LocalDateTime getDateTime(LocalDateTime dateTime) {
        return dateTime;
    }

    @Autowired
    public JdbcMealPostgres(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }
}