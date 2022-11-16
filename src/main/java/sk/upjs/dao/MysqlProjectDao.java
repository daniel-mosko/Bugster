package sk.upjs.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sk.upjs.entity.Project;
import sk.upjs.factory.DaoFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MysqlProjectDao implements ProjectDao {

    private final JdbcTemplate jdbcTemplate;

    public MysqlProjectDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Project getById(long id) {
        String sql = "select id,name,description from project where id=?";
        return jdbcTemplate.queryForObject(sql, new ProjectRowMapper(), id);
    }

    public List<Project> getByUserId(long id) {
        String sql = "select user_id,project_id from user_has_project where user_id=?";
        List<Long> projectsIds = jdbcTemplate.query(sql, new UserHasProjectRowMapper(), id);
        List<Project> projects = new ArrayList<>();
        for (Long projectsId : projectsIds) {
            projects.add(getById(projectsId));
        }
        return projects;
    }

    public Project addUserToProject(long userId, long projectId) {
        UserDao userDao = DaoFactory.INSTANCE.getUserDao();
        if (userDao.getById(userId) == null && getById(projectId) == null) {
            throw new NoSuchElementException();
        }
        SimpleJdbcInsert sjdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        sjdbcInsert.withTableName("user_has_project");
        sjdbcInsert.usingColumns("user_id", "project_id");

        Map<String, Object> values = new HashMap<>();
        values.put("user_id", userId);
        values.put("project_id", projectId);
        sjdbcInsert.execute(values);
        return getById(projectId);
    }

    public boolean deleteUserFromProject(long userId, long projectId) {
        String sql = "DELETE FROM user_has_project where user_id=? and project_id=?";
        int changed = jdbcTemplate.update(sql, userId, projectId);
        return changed == 1; // number of affected rows
    }

    public Project save(Project project) {
        if (project == null) throw new NullPointerException("cannot save null");
        if (project.getName() == null)
            throw new NullPointerException("Name of project is null");
        if (project.getId() == null) { // INSERT
            SimpleJdbcInsert sjdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            sjdbcInsert.withTableName("project");
            sjdbcInsert.usingGeneratedKeyColumns("id");
            sjdbcInsert.usingColumns("name", "description");

            Map<String, Object> values = new HashMap<>();
            values.put("name", project.getName());
            values.put("description", project.getDescription());

            long id = sjdbcInsert.executeAndReturnKey(values).longValue();
            return new Project(id, project.getName(), project.getDescription());
        } else { // UPDATE
            String sql = "UPDATE project SET name=?, description=? WHERE id = " + project.getId();
            int changed = jdbcTemplate.update(sql, project.getName(), project.getDescription());
            if (changed == 1) return project;
            throw new NoSuchElementException("Project with id " + project.getId() + " not in DB");
        }
    }

    public List<Project> getAll() {
        String sql = "select id,name,description from project";
        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    public boolean delete(long id) {
        int delete1 = jdbcTemplate.update("DELETE FROM user_has_project WHERE project_id=" + id);
        int delete2 = jdbcTemplate.update("DELETE FROM project WHERE id=" + id);
        return delete1 >= 1 && delete2 == 1; // number of affected rows
    }

    private static class UserHasProjectRowMapper implements RowMapper<Long> {
        public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getLong("project_id");
        }
    }

    private static class ProjectRowMapper implements RowMapper<Project> {
        public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
            Project project = new Project();
            project.setId(rs.getLong("id"));
            project.setName(rs.getString("name"));
            project.setDescription(rs.getString("description"));
            return project;
        }
    }
}