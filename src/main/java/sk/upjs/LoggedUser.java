package sk.upjs;

import sk.upjs.entity.User;

public enum LoggedUser {
    INSTANCE;

    private User loggedUser;

    LoggedUser() {
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
}
