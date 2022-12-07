package sk.upjs;

import sk.upjs.entity.User;


public enum LoggedUser {
    INSTANCE;
    private User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }


    public void setLoggedUser(User loggedUser) {
        System.out.println("Setting user");
        this.loggedUser = loggedUser;
    }
}
