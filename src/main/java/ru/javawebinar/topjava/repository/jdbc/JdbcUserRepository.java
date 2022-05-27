package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.preSave;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final ResultSetExtractor<List<User>> SET_EXTRACTOR = new UserListSetExtractor();
    private static final Comparator<User> USER_NAME_COMPARATOR = Comparator.comparing(User::getName).thenComparing(User::getEmail);
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
        preSave(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            saveRoles(user, false);
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            saveRoles(user, true);
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
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id =user_id WHERE id=?", SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", SET_EXTRACTOR);
    }

    private void saveRoles(User user, boolean update) {
        if (update) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
        }
        final List<Role> roles = new ArrayList<>(user.getRoles());
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?,?)", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.id());
                ps.setString(2, roles.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }

    static class UserListSetExtractor implements ResultSetExtractor<List<User>> {

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            //Collection<Role> roles = new ArrayList<>();
            Map<Integer, User> userMap = new LinkedHashMap<>();
            int rowNum = 1;
            while (rs.next()) {
                int userId = rs.getInt("id");
                int finalRowNum = rowNum;
                User user = userMap.computeIfAbsent(userId, id -> {
                    //roles.clear();
                    try {
                        return new User(ROW_MAPPER.mapRow(rs, finalRowNum));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
                final String role = rs.getString("role");
                if (role != null && user != null) {
                   // roles.add(Role.valueOf(role));
                    user.getRoles().add(Role.valueOf(role));
                }
                rowNum++;
            }
            return new ArrayList<>(userMap.values());
        }
    }
}