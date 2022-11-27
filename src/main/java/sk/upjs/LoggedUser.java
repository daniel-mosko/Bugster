package sk.upjs;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import sk.upjs.entity.User;


public enum LoggedUser {
    INSTANCE;

    /**
     * Role id = 1 -> Admin
     * 2 -> Project Manager
     * 3 -> Developer
     * Active id = 1 -> Active
     * 2 -> Inactive
     */
    private User loggedUser;

    @Bean
    public User getLoggedUser() {
        return loggedUser;
    }


    public void setLoggedUser(User loggedUser) {
        System.out.println("Setting user");
        this.loggedUser = loggedUser;
    }
}
