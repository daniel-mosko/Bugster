package sk.upjs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.Project;
import sk.upjs.factory.DaoFactory;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


class MysqlProjectDaoTest {


    private final ProjectDao projectDao;
    private Project savedProject;

    public MysqlProjectDaoTest() {
        this.projectDao = DaoFactory.INSTANCE.getProjectDao();
    }

    @BeforeEach
    void setUp() {
        Project project = new Project();
        project.setName("New project");
        project.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.");
        savedProject = projectDao.save(project);
        System.out.println(savedProject);
    }

    @AfterEach
    void tearDown() {
        projectDao.delete(savedProject.getId());
    }

    @Test
    void getAll() {
        List<Project> allProjects = projectDao.getAll();
        assertNotNull(allProjects);
        assertTrue(allProjects.size() > 0);
        assertNotNull(allProjects.get(0));
    }

    @Test
    void getById() {
        Project projectFromDb = projectDao.getById(savedProject.getId());
        assertEquals(projectFromDb.getId(), savedProject.getId());
        assertEquals(projectFromDb.getName(), savedProject.getName());
        assertThrows(EmptyResultDataAccessException.class, () -> projectDao.getById(-1));
    }

    @Test
    void getByUser() {
        List<Project> projects = projectDao.getByUser(1);
    }

    @Test
    void insert() {
        assertThrows(NullPointerException.class, () -> projectDao.save(null));
        int size = projectDao.getAll().size();

        Project project = new Project();
        project.setName("New");
        project.setDescription("New desc");
        Project saved = projectDao.save(project);
        assertEquals(size + 1, projectDao.getAll().size());
        assertNotNull(saved.getId());
        projectDao.delete(saved.getId());

        assertThrows(NullPointerException.class, () -> projectDao.save(new Project(null, null, null)));
    }

    @Test
    void update() {
        Project updated = new Project(savedProject.getId(), "Changed name", "Changed description");
        int size = projectDao.getAll().size();
        projectDao.save(updated);
        assertEquals(size, projectDao.getAll().size());

        Project fromDb = projectDao.getById(updated.getId());
        assertEquals(updated.getId(), fromDb.getId());
        assertEquals(updated.getName(), fromDb.getName());
        assertEquals(updated.getDescription(), fromDb.getDescription());

        assertThrows(NoSuchElementException.class, () -> projectDao.save(
                new Project(-1L, "Changed name", "Changed desc")));
    }

}