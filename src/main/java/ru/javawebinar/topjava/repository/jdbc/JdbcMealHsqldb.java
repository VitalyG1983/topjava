package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Repository
@Profile("jdbc & hsqldb")
public class JdbcMealHsqldb extends JdbcMealRepositoryAbstract<Timestamp> {

    @Override
    protected Timestamp getDateTime(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }

    @Autowired
    public JdbcMealHsqldb(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }
}