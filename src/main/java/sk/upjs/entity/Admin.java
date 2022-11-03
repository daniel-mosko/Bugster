package sk.upjs.entity;

public class Admin extends User{

    public Admin() {
    }

    public Admin(Long id, String name, String surname, String username, String password, String email, int role, boolean active) {
        super(id, name, surname, username, password, email, role, active);
    }

    /*
    * */
}
