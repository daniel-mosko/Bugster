package sk.upjs.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.bcrypt.BCrypt;
import sk.upjs.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MysqlUserDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public MysqlUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getById(long id) {
        String sql = "select id,name,surname,username,password,email,role_id,active from user where id=" + id;
        return jdbcTemplate.queryForObject(sql, new UserRowMapper());
    }

    public List<User> getByProject(long id) {
        String sql = "select user_id from user_has_project where project_id=?";
        List<Long> usersIds = jdbcTemplate.query(sql, new UserHasProjectRowMapper(), id);
        List<User> users = new ArrayList<>();
        for (Long userId : usersIds) {
            users.add(getById(userId));
        }
        return users;
    }

    public List<User> getAll() {
        String sql = "select id,name,surname,username,password,email,role_id,active from user";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public User save(User user) {
        if (user == null) throw new NullPointerException("cannot save null");
        if (user.getName() == null || user.getSurname() == null || user.getPassword() == null || user.getRole() == 0 || user.getUsername() == null || user.getEmail() == null)
            throw new NullPointerException("Name, surname, username, password, role or email is null");
        if (user.getId() == null) { // INSERT
            String salt = BCrypt.gensalt();
            user.setPassword(BCrypt.hashpw(user.getPassword(), salt));

            SimpleJdbcInsert sjdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            sjdbcInsert.withTableName("user");
            sjdbcInsert.usingGeneratedKeyColumns("id");
            sjdbcInsert.usingColumns("name", "surname", "username", "password", "email", "role_id", "active");

            Map<String, Object> values = new HashMap<>();
            values.put("name", user.getName());
            values.put("surname", user.getSurname());
            values.put("username", user.getUsername());
            values.put("password", user.getPassword());
            values.put("email", user.getEmail());
            values.put("role_id", user.getRole());
            values.put("active", user.isActive());

            long id = sjdbcInsert.executeAndReturnKey(values).longValue();
            return new User(id, user.getName(), user.getSurname(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.isActive());
        } else { // UPDATE
            /*
             * PORIESIT ESTE HESLO!
             */
            String sql = "UPDATE user SET name=?,surname=?,username=?,password=?,email=?,role_id=?,active=? WHERE id = " + user.getId();
            int changed = jdbcTemplate.update(sql, user.getName(), user.getSurname(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.isActive());
            if (changed == 1) return user;
            throw new NoSuchElementException("User with id " + user.getId() + " not in DB");
        }
    }

    /***
     *
     * @param id - user id
     * @return if delete was successful
     */
    public boolean delete(long id) {
        int changed = jdbcTemplate.update("DELETE FROM user where id=" + id);
        return changed == 1; // number of affected rows
    }

    private static class UserRowMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getInt("role_id"));
            user.setActive(rs.getBoolean("active"));
            return user;
        }
    }

    private static class UserHasProjectRowMapper implements RowMapper<Long> {
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong("user_id");
        }
    }
}
