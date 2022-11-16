package sk.upjs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.Project;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class MysqlUserDaoTest {

    private final UserDao userDao;
    private final ProjectDao projectDao;
    private User savedUser;

    public MysqlUserDaoTest() {
        //DaoFactory.INSTANCE.setTesting();
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
        user.setRole(3);
        user.setActive(true);
        savedUser = userDao.save(user);
    }

    @AfterEach
    void tearDown() {
        userDao.delete(savedUser.getId());
    }

    @Test
    void getAll() {
        List<User> allUsers = userDao.getAll();
        assertNotNull(allUsers);
        assertTrue(allUsers.size() > 0);
        assertNotNull(allUsers.get(0));
    }

    @Test
    void getById() {
        User userFromDb = userDao.getById(savedUser.getId());
        assertEquals(userFromDb.getId(), savedUser.getId());
        assertEquals(userFromDb.getUsername(), savedUser.getUsername());
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.getById(-1));
    }

    @Test
    void getByProjectId() {
        Project project = new Project();
        project.setName("Test");
        project.setDescription("Test");
        Project savedProject = projectDao.save(project);
        projectDao.addUserToProject(savedUser.getId(), savedProject.getId());

        List<User> users = userDao.getByProjectId(savedProject.getId())
                .stream()
                .filter(u -> u.getId().equals(savedUser.getId()))
                .toList();
        // Only 1 with given ID can be assigned to project
        assertEquals(1, users.size());
        // Check if saved user is assigned to project
        assertEquals(savedUser.getId(), users.get(0).getId());
        projectDao.delete(savedProject.getId());
    }

    @Test
    void insert() {
        assertThrows(NullPointerException.class, () -> userDao.save(null));
        int size = userDao.getAll().size();

        User user = new User();
        user.setName("New Jakub");
        user.setSurname("New Testovic");
        user.setUsername("New jtest");
        user.setPassword("pass123");
        user.setEmail("new@test.com");
        user.setRole(3);
        user.setActive(true);

        User saved = userDao.save(user);
        assertEquals(size + 1, userDao.getAll().size());
        assertNotNull(saved.getId());
        userDao.delete(saved.getId());

        assertThrows(NullPointerException.class, () -> userDao.save(new User(null, null, null,
                null, null, null, 0, false)));
    }

    @Test
    void update() {
        User updated = new User(savedUser.getId(), "Changed name", "Changed surname",
                "Changed username", "Changed password", "Changed email", 2, false);
        int size = userDao.getAll().size();
        userDao.save(updated);
        assertEquals(size, userDao.getAll().size());

        User fromDb = userDao.getById(updated.getId());
        assertEquals(updated.getId(), fromDb.getId());
        assertEquals(updated.getName(), fromDb.getName());
        assertEquals(updated.getSurname(), fromDb.getSurname());
        assertEquals(updated.getUsername(), fromDb.getUsername());
        assertEquals(updated.getEmail(), fromDb.getEmail());
        assertEquals(updated.getRole(), fromDb.getRole());
        assertEquals(updated.isActive(), fromDb.isActive());

        assertThrows(NullPointerException.class, () -> userDao.save(new User(-1L, "Changed",
                "Changed", "Changed", "Changed", "Changed", 0, false)));
    }
}