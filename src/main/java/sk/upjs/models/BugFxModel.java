package sk.upjs.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sk.upjs.dao.BugDao;
import sk.upjs.dao.ProjectDao;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.*;
import sk.upjs.factory.DaoFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BugFxModel {
    private Long id;
    private StringProperty description = new SimpleStringProperty();
    private ObjectProperty<Project> project = new SimpleObjectProperty<>();
    private ObjectProperty<String> created_at= new SimpleObjectProperty<>();
    private ObjectProperty<String> updated_at= new SimpleObjectProperty<>();
    private ObjectProperty<User> assigner = new SimpleObjectProperty<>();
    private ObjectProperty<User> assignee = new SimpleObjectProperty<>();
    private ObjectProperty<Status> status = new SimpleObjectProperty<>();
    private ObjectProperty<Severity> severity = new SimpleObjectProperty<>();
    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();
    private BugDao bugDao = DaoFactory.INSTANCE.getBugDao();
    private ProjectDao projectDao = DaoFactory.INSTANCE.getProjectDao();

    public BugFxModel() {
    }

    public BugFxModel(Bug bug) {
        this.id = bug.getId();
        description.set(bug.getDescription());
        project.set(projectDao.getById(bug.getProjectId()));

        created_at.set(getCreated_at());
        updated_at.set(getUpdated_at());
        created_at.set(bug.getCreatedAt());
        updated_at.set(bug.getUpdatedAt());
        assigner.set(userDao.getById(bug.getAssignerId()));
        assignee.set(userDao.getById(bug.getAssigneeId()));
        status.set(bugDao.getStatusById(bug.getStatusId()));
        severity.set(bugDao.getSeverityById(bug.getSeverityId()));
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public Project getProject() {
        return project.get();
    }

    public void setProject(Project project) {
        this.project.set(project);
    }

    public ObjectProperty<Project> projectProperty() {
        return project;
    }

    public String getCreated_at() {
        return created_at.get();
    }

    public void setCreated_at(String created_at) {
        this.created_at.set(created_at);
    }

    public ObjectProperty<String> created_atProperty() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at.get();
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at.set(updated_at);
    }

    public ObjectProperty<String> updated_atProperty() {
        return updated_at;
    }

    public User getAssigner() {
        return assigner.get();
    }

    public void setAssigner(User assigner) {
        this.assigner.set(assigner);
    }

    public ObjectProperty<User> assignerProperty() {
        return assigner;
    }

    public User getAssignee() {
        return assignee.get();
    }

    public void setAssignee(User assignee) {
        this.assignee.set(assignee);
    }

    public ObjectProperty<User> assigneeProperty() {
        return assignee;
    }

    public Status getStatus() {
        return status.get();
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    public Severity getSeverity() {
        return severity.get();
    }

    public void setSeverity(Severity severity) {
        this.severity.set(severity);
    }

    public ObjectProperty<Severity> severityProperty() {
        return severity;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public BugDao getBugDao() {
        return bugDao;
    }

    public void setBugDao(BugDao bugDao) {
        this.bugDao = bugDao;
    }

    public Bug getBug() {
        return new Bug(id, getDescription(), getCreated_at(), getUpdated_at(), getProject().getId(), getAssigner().getId(), getAssignee().getId(), getStatus().getId(), getSeverity().getId());
    }

}
