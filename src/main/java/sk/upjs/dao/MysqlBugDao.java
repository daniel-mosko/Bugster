package sk.upjs.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import sk.upjs.entity.Bug;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MysqlBugDao implements BugDao {

    private JdbcTemplate jdbcTemplate;

    public MysqlBugDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Bug getById(long id) {
        return null;
    }

    public List<Bug> getAll() {
        String sql = "SELECT id,description,created_at,updated_at,project_id,assigner_id,assignee_id,status_id,severity_id FROM bug";
        return jdbcTemplate.query(sql, new RowMapper<Bug>() {
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
        });
    }

    public Bug save(Bug bug) {
        return null;
    }

    public boolean delete(long id) {
        return false;
    }
}
