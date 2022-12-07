package sk.upjs.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.Role;
import sk.upjs.entity.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserDao {
    User getById(long id) throws EmptyResultDataAccessException;

    User getByUsername(String username);

    List<User> getAll();


    User save(User User) throws NullPointerException, NoSuchElementException, UnauthorizedAccessException;

    List<User> getByProjectId(long id) throws NoSuchElementException;

    List<Role> getAllRoles();

    boolean delete(long id) throws NoSuchElementException, UnauthorizedAccessException;
}
