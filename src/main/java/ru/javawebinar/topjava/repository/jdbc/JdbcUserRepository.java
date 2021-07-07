package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Role> roles = new ArrayList<>(user.getRoles());
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            int update = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource);
            if (update == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.id());
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (role, user_id) VALUES(?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setString(1, roles.get(i).name());
                        ps.setInt(2, user.id());
                    }
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.enabled, u.calories_per_day, u.registered, r.role " +
                        " FROM users u JOIN user_roles r ON r.user_id = u.id WHERE u.id=?",
                preparedStatement -> preparedStatement.setInt(1, id),
                getResultSetExtractor());
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.id, u.name, u.email, u.password, u.enabled, u.calories_per_day, u.registered, r.role " +
                        " FROM users u JOIN user_roles r ON r.user_id = u.id WHERE u.email=?",
                preparedStatement -> preparedStatement.setString(1, email),
                getResultSetExtractor());
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        PreparedStatementCreator preparedStatementCreator =
                connection -> connection.prepareStatement("SELECT u.id, u.name, u.email, u.password, u.enabled, u.calories_per_day, u.registered, r.role " +
                        " FROM users u JOIN user_roles r ON r.user_id = u.id");
        return jdbcTemplate.query(preparedStatementCreator, getResultSetExtractor());
    }

    private ResultSetExtractor<List<User>> getResultSetExtractor() {
        return rs -> {
            Map<Integer, User> users = new HashMap<>();
            while (rs.next()) {
                int userId = rs.getInt("id");
                User user = new User(userId,
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role")));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getDate("registered"));
                users.merge(userId, user, (v1, v2) -> {
                    v2.getRoles().addAll(v1.getRoles());
                    return v2;
                });
            }
            return new ArrayList<>(users.values());
        };
    }
}
