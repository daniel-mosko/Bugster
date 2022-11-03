package sk.upjs.entity;

public class ProjectManager extends User {
    public ProjectManager() {
    }

    public ProjectManager(Long id, String name, String surname, String username, String password, String email, int role, boolean active) {
        super(id, name, surname, username, password, email, role, active);
    }

    /*
     * CREATE project
     */
}
