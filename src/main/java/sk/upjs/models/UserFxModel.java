package sk.upjs.models;

import javafx.beans.property.*;
import sk.upjs.dao.UserDao;
import sk.upjs.entity.Role;
import sk.upjs.entity.User;
import sk.upjs.factory.DaoFactory;

public class UserFxModel {

    private Long id;
    private StringProperty name = new SimpleStringProperty();
    private StringProperty surname = new SimpleStringProperty();
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private ObjectProperty<Role> role = new SimpleObjectProperty<>();
    private BooleanProperty active = new SimpleBooleanProperty();

    private UserDao userDao = DaoFactory.INSTANCE.getUserDao();

    public UserFxModel(User user) {
        this.id = user.getId();
        name.set(user.getName());
        surname.set(user.getSurname());
        username.set(user.getUsername());
        password.set(user.getPassword()); // zvazit
        email.set(user.getEmail());
        active.set(user.isActive());
        role.set(userDao.getByRoleId(user.getRole_id()));
    }

    public UserFxModel() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public StringProperty surnameProperty() {
        return surname;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }


    public Role getRole() {
        return role.get();
    }

    public void setRole(Role role) {
        this.role.set(role);
    }

    public ObjectProperty<Role> roleProperty() {
        return role;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public User getUser() {
        return new User(getId(), getName(), getSurname(), getUsername(), getPassword(), getEmail(), getRole().getId(), isActive());
    }
}
