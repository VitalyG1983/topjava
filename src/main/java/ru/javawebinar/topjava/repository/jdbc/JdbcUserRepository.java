package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.preSave;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Role> roles = user.getRoles().stream().toList();
        String sqlRoles = "INSERT INTO user_roles (user_id, role) VALUES (?,?)";
        if (user.isNew()) {
            preSave(user);
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            saveRoles(sqlRoles, roles, newKey.intValue(), false);
        } else {
            preSave(user);
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            saveRoles(sqlRoles, roles, user.getId(), true);
        }
        return user;
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id =user_id WHERE id=?", new UserRowMapper(), id);
        return users.size() != 0 ? users.get(users.size() - 1) : null;
        //return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", new UserRowMapper(), email);
        return users.size() != 0 ? users.get(users.size() - 1) : null;
        //return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        final UserRowMapper userRowMapper = new UserRowMapper();
        jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", userRowMapper);
        return userRowMapper.userRolesMap.values().stream().toList();
    }

    private void saveRoles(String sql, List<Role> roles, int userId, boolean update) {
        if (update) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", userId);
        }
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, userId);
                ps.setString(2, roles.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }

    static class UserRowMapper implements RowMapper<User> {
        private Collection<Role> roles = new ArrayList<>();
        private Map<Integer, User> userRolesMap = new LinkedHashMap<>();
        private int oldUserId = 0;

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            int userId = rs.getInt("id");
            final String role = rs.getString("role");
            if (rowNum > 0 && (oldUserId > userId || oldUserId < userId)) {
                roles.clear();
            }
            if (role != null) {
                roles.add(Role.valueOf(role));
            }
            userRolesMap.put(userId, user);
            user.setRoles(roles);
            user.setId(userId);
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setRegistered(Date.from(Timestamp.valueOf(rs.getString("registered")).toInstant()));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
            oldUserId = userId;
            return user;
        }
    }
}