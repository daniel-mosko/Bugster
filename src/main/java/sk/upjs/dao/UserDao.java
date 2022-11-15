package sk.upjs.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserDao {
    User getById(long id) throws EmptyResultDataAccessException;

    List<User> getAll();

    User save(User User) throws NullPointerException, NoSuchElementException;

    List<User> getByProject(long id);

    boolean delete(long id);
}