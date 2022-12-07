package sk.upjs.business;

import sk.upjs.dao.ProjectDao;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.Project;
import sk.upjs.factory.DaoFactory;

import java.util.List;

public class ProjectsManager {

    List<Project> projectList;
    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private ProjectDao projectDao = DaoFactory.INSTANCE.getProjectDao();

    public ProjectsManager() {
        List<Project> p = projectDao.getAll();
        for (Project project : p) {
            ProjectStatistics projectStatistics = new ProjectStatistics(project);
            projectStatistics.setUserList(userDao.getByProjectId(project.getId()));
            projectList.add(projectStatistics);
        }
    }

    public List<Project> getProjectList() {
        return projectList;
    }
}
