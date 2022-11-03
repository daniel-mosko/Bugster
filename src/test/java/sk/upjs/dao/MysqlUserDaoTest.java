package sk.upjs.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

class MysqlUserDaoTest {

    private User savedUser;
    private UserDao userDao;

    public MysqlUserDaoTest() {
        //DaoFactory.INSTANCE.setTesting();
        userDao = DaoFactory.INSTANCE.getUserDao();
    }

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setActive(true);
        user.setEmail("test@test.com");
        user.setName("Jakub");
        user.setSurname("Testovic");
        user.setUsername("jtest");
        user.setPassword("pass123");
        user.setRole(1);
        savedUser = userDao.save(user);
    }

    @AfterEach
    void tearDown() {
    }
}