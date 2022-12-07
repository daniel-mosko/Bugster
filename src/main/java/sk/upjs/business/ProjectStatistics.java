package sk.upjs.business;

import sk.upjs.entity.Project;
import sk.upjs.entity.User;

import java.util.List;

public class ProjectStatistics extends Project {

    private List<User> userList;

    public ProjectStatistics(Project project) {
        super(project.getId(), project.getName(), project.getDescription());
    }

    public ProjectStatistics(Long id, String name, String description) {
        super(id, name, description);
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
