package sk.upjs.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.Role;
import sk.upjs.entity.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserDao {
    /**
     * @param id user id
     * @return User object from DB
     * @throws EmptyResultDataAccessException if user not in DB
     */
    User getById(long id) throws EmptyResultDataAccessException;

    /**
     * @param username User username
     * @return User object
     */
    User getByUsername(String username);

    /**
     * @return List of all users
     */
    List<User> getAll();


    /**
     * @param User user object
     * @return saved / updated user
     * @throws NullPointerException        if user is null or Name, surname, username, password, role or email is null
     * @throws NoSuchElementException      if user with given id is not in DB
     * @throws UnauthorizedAccessException if logged user has no privileges to save / update user
     */
    User save(User User) throws NullPointerException, NoSuchElementException, UnauthorizedAccessException;

    /**
     * @param id project id
     * @return List of users that are assigned to project with given id
     * @throws NoSuchElementException if project with given id is not in DB
     */
    List<User> getByProjectId(long id) throws NoSuchElementException;

    /**
     * @return List of all roles
     */
    List<Role> getAllRoles();

    /**
     * @param id Role id
     * @return Role object
     */
    Role getByRoleId(long id);

    /**
     * @param id User id
     * @return true / false if delete was successful
     * @throws NoSuchElementException      if user with given id is not in DB
     * @throws UnauthorizedAccessException if logged user has no privileges to delete user
     */
    boolean delete(long id) throws NoSuchElementException, UnauthorizedAccessException;
}
