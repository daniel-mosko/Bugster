package sk.upjs.dao;

import sk.upjs.entity.User;

import java.util.List;

public interface UserDao {
    User getById(long id);

    List<User> getAll();

    User save(User User);

    boolean delete(long id);
}
