package sk.upjs.factory;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.upjs.dao.BugDao;
import sk.upjs.dao.MysqlBugDao;
import sk.upjs.dao.MysqlUserDao;
import sk.upjs.dao.UserDao;

public enum DaoFactory {
    INSTANCE;
    private JdbcTemplate jdbcTemplate;

    private BugDao bugDao;
    private UserDao userDao;
    private boolean testing = false;


    public void setTesting() {
        this.testing = true;
    }

    public BugDao getBugDao() {
        if (bugDao == null) {
            bugDao = new MysqlBugDao(getJdbcTemplate());
        }
        return bugDao;
    }

    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new MysqlUserDao(getJdbcTemplate());
        }
        return userDao;
    }

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            if (testing) {
                dataSource.setDatabaseName("bugster2022test");
                dataSource.setUser("bugster2022test");
                dataSource.setPassword("test");
            } else {
                dataSource.setDatabaseName("bugster2022");
                dataSource.setUser("bugster2022");
                dataSource.setPassword("eE87#H06g");
            }
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }
}
