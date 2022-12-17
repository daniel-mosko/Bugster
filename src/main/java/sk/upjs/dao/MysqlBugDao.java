package sk.upjs.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sk.upjs.LoggedUser;
import sk.upjs.entity.Bug;
import sk.upjs.entity.Severity;
import sk.upjs.entity.Status;
import sk.upjs.factory.DaoFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class MysqlBugDao implements BugDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    public MysqlBugDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Bug getById(long id) {
        String sql = "select id,description,created_at,updated_at,project_id,assignee_id,assigner_id,status_id,severity_id from bug where id=" + id;
        return jdbcTemplate.queryForObject(sql, new BugRowMapper());
    }

    @Override
    public List<Bug> getAll() {
        String sql = "SELECT id,description,created_at,updated_at,project_id,assigner_id,assignee_id,status_id,severity_id FROM bug";
        return jdbcTemplate.query(sql, new BugRowMapper());
    }


    @Override
    public Bug save(Bug bug) throws NoSuchElementException, NullPointerException, UnauthorizedAccessException {
        if (LoggedUser.INSTANCE.getLoggedUser().getRole_id() != 1 && LoggedUser.INSTANCE.getLoggedUser().getRole_id() != 2)
            throw new UnauthorizedAccessException("Unauthorized - only admin and project manager can save bug");
        if (bug == null) throw new NullPointerException("cannot save null");
        if (bug.getId() == null) { // INSERT
            SimpleJdbcInsert sjdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            sjdbcInsert.withTableName("bug");
            sjdbcInsert.usingGeneratedKeyColumns("id");
            sjdbcInsert.usingColumns("description", "created_at", "updated_at", "project_id", "assignee_id", "assigner_id", "status_id", "severity_id");

            Map<String, Object> values = new HashMap<>();
            values.put("description", bug.getDescription());
            values.put("created_at", bug.getCreatedAt());
            values.put("updated_at", bug.getUpdatedAt());
            values.put("project_id", bug.getProjectId());
            values.put("assignee_id", bug.getAssigneeId());
            values.put("assigner_id", bug.getAssignerId());
            values.put("status_id", bug.getStatusId());
            values.put("severity_id", bug.getSeverityId());

            Long id = sjdbcInsert.executeAndReturnKey(values).longValue(); // returns id of inserted bug
            return new Bug(id, bug.getDescription(), bug.getCreatedAt(), bug.getUpdatedAt(), bug.getProjectId(), bug.getAssignerId(), bug.getAssigneeId(), bug.getStatusId(), bug.getSeverityId());
        } else { // UPDATE
            if (bug.getId() <= 0) throw new NoSuchElementException("Id cannot be negative");
            String sql = "UPDATE bug SET description=?,created_at=?,updated_at=?,project_id=?,assigner_id=?,assignee_id=?,status_id=?,severity_id=? WHERE id = ?";
            int changed = jdbcTemplate.update(sql, bug.getDescription(), bug.getCreatedAt(), bug.getUpdatedAt(), bug.getProjectId(), bug.getAssignerId(), bug.getAssigneeId(), bug.getStatusId(), bug.getSeverityId(), bug.getId());
            if (changed == 1) return bug;
            throw new NoSuchElementException("Bug with id " + bug.getId() + " not in DB");
        }
    }

    @Override
    public Bug changeStatus(long bugId, long statusId) throws NoSuchElementException {
        if (getStatusById(statusId) == null)
            throw new NoSuchElementException("Status with id " + statusId + " not in DB");
        String sql = "UPDATE bug SET status_id=? WHERE id = " + bugId;
        int changed = jdbcTemplate.update(sql, statusId);
        if (changed == 1) return getById(bugId);
        throw new NoSuchElementException("Bug with id " + bugId + " not in DB");
    }

    @Override
    public boolean delete(long id) throws UnauthorizedAccessException {
        if (LoggedUser.INSTANCE.getLoggedUser().getRole_id() != 1 && LoggedUser.INSTANCE.getLoggedUser().getRole_id() != 2)
            throw new UnauthorizedAccessException("Unauthorized - only admin and project manager can delete bug");
        int changed = jdbcTemplate.update("DELETE FROM bug where id=" + id);
        return changed == 1; // number of affected rows
    }

    @Override
    public List<Status> getAllStatuses() {
        String sql = "select id,name from status";
        return jdbcTemplate.query(sql, new StatusRowMapper());
    }

    @Override
    public List<Severity> getAllSeverities() {
        String sql = "select id, severity_level, name from severity";
        return jdbcTemplate.query(sql, new SeverityRowMapper());
    }

    @Override
    public Severity getSeverityById(long id) {
        String sql = "select id, severity_level, name from severity where id=?";
        return jdbcTemplate.queryForObject(sql, new SeverityRowMapper(), id);
    }

    @Override
    public Status getStatusById(long id) {
        String sql = "select id,name from status where id=?";
        return jdbcTemplate.queryForObject(sql, new StatusRowMapper(), id);
    }


    private static class BugRowMapper implements RowMapper<Bug> {
        public Bug mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bug bug = new Bug();
            bug.setId(rs.getLong("id"));
            bug.setProjectId(rs.getLong("project_id"));
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

    private static class StatusRowMapper implements RowMapper<Status> {
        public Status mapRow(ResultSet rs, int rowNum) throws SQLException {
            Status status = new Status();
            status.setId(rs.getInt("id"));
            status.setName(rs.getString("name"));
            return status;
        }
    }

    private static class SeverityRowMapper implements RowMapper<Severity> {
        public Severity mapRow(ResultSet rs, int rowNum) throws SQLException {
            Severity severity = new Severity();
            severity.setId(rs.getInt("id"));
            severity.setSeverityLevel(rs.getInt("severity_level"));
            severity.setName(rs.getString("name"));
            return severity;
        }
    }
}
