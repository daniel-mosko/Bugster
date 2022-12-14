package sk.upjs.entity;

public class User {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private int role_id;
    private boolean active;

    /**
     * Role id
     * 1 -> Admin
     * 2 -> Project Manager
     * 3 -> Developer
     * Active id
     * 1 -> Active
     * 2 -> Inactive
     */

    public User() {
    }

    public User(Long id, String name, String surname, String username, String password, String email, int role_id, boolean active) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role_id = role_id;
        this.active = active;
    }

    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.surname = user.surname;
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.role_id = user.role_id;
        this.active = user.active;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    /*
    @Override
    public String toString() {
        return name + " " + surname;
    }

 */
}
