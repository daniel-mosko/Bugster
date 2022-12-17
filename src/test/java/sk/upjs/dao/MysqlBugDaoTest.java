package sk.upjs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.LoggedUser;
import sk.upjs.entity.Bug;
import sk.upjs.entity.Project;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MysqlBugDaoTest {

    private final BugDao bugDao;
    private final UserDao userDao;
    private final ProjectDao projectDao;
    private Bug savedBug;
    private User savedAssigner;

    private User savedAssignee;

    private Project savedProject;

    public MysqlBugDaoTest() {
        //DaoFactory.INSTANCE.setTesting();
        bugDao = DaoFactory.INSTANCE.getBugDao();
        userDao = DaoFactory.INSTANCE.getUserDao();
        projectDao = DaoFactory.INSTANCE.getProjectDao();
    }


    @BeforeEach
    void setUp() {
        User user = new User();
        user.setName("Jakub");
        user.setSurname("Testovic");
        user.setUsername("jtest");
        user.setPassword("pass123");
        user.setEmail("test@test.com");
        user.setRole_id(1);
        user.setActive(true);
        LoggedUser.INSTANCE.setLoggedUser(new User(user));
        savedProject = projectDao.save(new Project(null, "testName", "testDescription"));
        savedAssigner = userDao.save(new User(null, "Jakub", "Testovic", "jtest",
                "pass123", "test@test.com", 2, true));

        savedAssignee = userDao.save(new User(null, "Daniel", "Testovic", "jtest",
                "pass123", "test@test.com", 2, true));

        Bug bug = new Bug();
        bug.setDescription("Please change font to comic sens :D");
        long now = System.currentTimeMillis();
        Timestamp sqlTimestamp = new Timestamp(now);
        bug.setCreatedAt(sqlTimestamp.toString());
        bug.setProjectId(savedProject.getId());
        bug.setAssignerId(savedAssigner.getId());
        bug.setAssigneeId(savedAssignee.getId());
        bug.setStatusId(1);
        bug.setSeverityId(1);
        savedBug = bugDao.save(bug);
    }

    @AfterEach
    void tearDown() {
        bugDao.delete(savedBug.getId());
        userDao.delete(savedAssignee.getId());
        userDao.delete(savedAssigner.getId());
        projectDao.delete(savedProject.getId());
    }

    @Test
    void getById() {
        Bug bugFromDb = bugDao.getById(savedBug.getId());
        assertEquals(bugFromDb.getId(), savedBug.getId()); // bugId recieved from db equals saved bugId
        assertThrows(EmptyResultDataAccessException.class, () -> bugDao.getById(-1));
    }

    @Test
    void getAll() {
        List<Bug> allBugs = bugDao.getAll();
        assertNotNull(allBugs);
        assertTrue(allBugs.size() > 0);
        assertNotNull(allBugs.get(0));
    }

    @Test
    void insert() {
        assertThrows(NullPointerException.class, () -> bugDao.save(null)); // null object save
        int size = bugDao.getAll().size();
        Project savedProject = projectDao.save(new Project(null, "testName", "testDescription")); // save project
        User savedAssigner = userDao.save(new User(null, "Jakub", "Testovic", "jtest",
                "pass123", "test@test.com", 2, true)); // save assigner

        User savedAssignee = userDao.save(new User(null, "Daniel", "Testovic", "jtest", // save assignee
                "pass123", "test@test.com", 2, true));

        // insert new bug
        Bug bug = new Bug();
        bug.setDescription("Please change font to comic sens :D");
        long now = System.currentTimeMillis();
        Timestamp sqlTimestamp = new Timestamp(now);
        bug.setCreatedAt(sqlTimestamp.toString());
        bug.setProjectId(savedProject.getId());
        bug.setAssignerId(savedAssigner.getId());
        bug.setAssigneeId(savedAssignee.getId());
        bug.setStatusId(1);
        bug.setSeverityId(1);
        Bug saved = bugDao.save(bug);
        assertEquals(size + 1, bugDao.getAll().size());

        // check if saved bug with given id exists in DB
        assertNotNull(saved.getId());
        // delete test
        bugDao.delete(saved.getId());

        projectDao.delete(savedProject.getId());
        userDao.delete(savedAssigner.getId());
        userDao.delete(savedAssignee.getId());

        assertThrows(DataIntegrityViolationException.class, () -> bugDao.save(new Bug(null, null, null,
                null, 0, 0, 0, 0, 0)));
    }

    @Test
    void changeStatus() {
        assertEquals(bugDao.changeStatus(savedBug.getId(), 2).getStatusId(), 2);
    }

    @Test
    void update() {
        savedProject = projectDao.save(new Project(savedProject.getId(), "Changed testName", "Changed testDescription"));
        savedAssigner = userDao.save(new User(savedAssigner.getId(), "Changed assigner", "Changed surname",
                "Changed username", "Changed password", "Changed email", 2, true));

        savedAssignee = userDao.save(new User(savedAssignee.getId(), "Changed assignee", "Changed surname",
                "Changed username", "Changed password", "Changed email", 2, true));

        Bug updated = new Bug(savedBug.getId(), "Changed discription", "2022-05-04 16:54:42",
                "2022-06-04 05:24:23", savedProject.getId(), savedAssigner.getId(), savedAssignee.getId(), 1, 1);
        int size = bugDao.getAll().size(); // number of bugs in DB
        bugDao.save(updated);
        // check if number of bugs in DB is the same after update
        assertEquals(size, bugDao.getAll().size());

        Bug fromDb = bugDao.getById(updated.getId());
        assertEquals(updated.getId(), fromDb.getId());
        assertEquals(updated.getDescription(), fromDb.getDescription());
        assertEquals(updated.getCreatedAt(), fromDb.getCreatedAt());
        assertEquals(updated.getUpdatedAt(), fromDb.getUpdatedAt());
        assertEquals(updated.getProjectId(), fromDb.getProjectId());
        assertEquals(updated.getAssignerId(), fromDb.getAssignerId());
        assertEquals(updated.getAssigneeId(), fromDb.getAssigneeId());
        assertEquals(updated.getStatusId(), fromDb.getStatusId());
        assertEquals(updated.getSeverityId(), fromDb.getSeverityId());

        assertThrows(NoSuchElementException.class, () -> bugDao.save(new Bug(-1L, "Changed discription", "Changed created_at",
                "Changed updated_at", savedProject.getId(), savedAssigner.getId(), savedAssignee.getId(), 1, 1)));
    }
}