package sk.upjs.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sk.upjs.entity.Bug;
import sk.upjs.entity.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class MysqlBugDao implements BugDao {

    private final JdbcTemplate jdbcTemplate;

    public MysqlBugDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Bug getById(long id) {
        String sql = "select id,description,created_at,updated_at,project_id,assignee_id,assigner_id,status_id,severity_id from bug where id=" + id;
        return jdbcTemplate.queryForObject(sql, new BugRowMapper());
    }

    public List<Bug> getAll() {
        String sql = "SELECT id,description,created_at,updated_at,project_id,assigner_id,assignee_id,status_id,severity_id FROM bug";
        return jdbcTemplate.query(sql, new BugRowMapper());
    }

    public Bug save(Bug bug) {
        if (bug == null) throw new NullPointerException("cannot save null");
        if (bug.getId() == null) { // INSERT
            SimpleJdbcInsert sjdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            sjdbcInsert.withTableName("bug");
            sjdbcInsert.usingGeneratedKeyColumns("id");
            sjdbcInsert.usingColumns("description", "created_at", "updated_at", "project_id",
                    "assignee_id", "assigner_id", "status_id", "severity_id");

            Map<String, Object> values = new HashMap<>();
            values.put("description", bug.getDescription());
            values.put("created_at", bug.getCreatedAt());
            values.put("updated_at", bug.getUpdatedAt());
            values.put("project_id", bug.getProjectId());
            values.put("assignee_id", bug.getAssigneeId());
            values.put("assigner_id", bug.getAssignerId());
            values.put("status_id", bug.getStatusId());
            values.put("severity_id", bug.getSeverityId());

            Long id = sjdbcInsert.executeAndReturnKey(values).longValue();
            return new Bug(id, bug.getDescription(), bug.getCreatedAt(), bug.getUpdatedAt(), bug.getProjectId(),
                    bug.getAssignerId(), bug.getAssigneeId(), bug.getStatusId(), bug.getSeverityId());
        } else { // UPDATE
            String sql = "UPDATE bug SET name=?, description=? WHERE id = " + bug.getId();
            int changed = jdbcTemplate.update(sql, bug.getDescription(), bug.getCreatedAt(), bug.getUpdatedAt(), bug.getProjectId(),
                    bug.getAssignerId(), bug.getAssigneeId(), bug.getStatusId(), bug.getSeverityId());
            if (changed == 1) return bug;
            throw new NoSuchElementException("Bug with id " + bug.getId() + " not in DB");
        }
    }

    public boolean delete(long id) {
        int changed = jdbcTemplate.update("DELETE FROM bug where id=" + id);
        return changed == 1; // number of affected rows
    }

    private static class BugRowMapper implements RowMapper<Bug> {
        public Bug mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bug bug = new Bug();
            bug.setId(rs.getLong("id"));
            bug.setDescription(rs.getString("description"));
            bug.setCreatedAt(rs.getString("created_at"));
            bug.setUpdatedAt(rs.getString("updated_at"));
            bug.setAssignerId(rs.getLong("assigner_id"));
            bug.setAssigneeId(rs.getLong("assignee_id"));
            bug.setStatusId(rs.getLong("status_id"));
            bug.setSeverityId(rs.getLong("severity_id"));
            return bug;
        }
    }
}
