package sk.upjs.dao;

import org.junit.jupiter.api.BeforeEach;
import sk.upjs.dao.BugDao;
import sk.upjs.entity.Bug;
import sk.upjs.factory.DaoFactory;

class MysqlBugDaoTest {
    private BugDao bugDao;
    private Bug savedBug;

    public MysqlBugDaoTest() {
        DaoFactory.INSTANCE.setTesting();
        bugDao=DaoFactory.INSTANCE.getBugDao();
    }

    @BeforeEach
    void setUp() throws Exception{
        Bug bug = new Bug();
        bug.setDescription("bug1");
        bug.setAssignerId(1);
        bug.setAssigneeId(1);

    }

}
