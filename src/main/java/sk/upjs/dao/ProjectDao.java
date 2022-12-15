package sk.upjs.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.Project;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProjectDao {
    /**
     * @param id project id
     * @return Project object from DB
     * @throws NoSuchElementException         if project with given id is not in database
     * @throws EmptyResultDataAccessException if result from database is empty
     */
    Project getById(long id) throws NoSuchElementException, EmptyResultDataAccessException;

    /**
     * @param id user id
     * @return list of Projects which are assigned to user
     * @throws NoSuchElementException if user with given id is not in database
     */

    List<Project> getByUserId(long id) throws NoSuchElementException;

    /**
     * @param userId    User id
     * @param projectId Project id
     * @return Project that user with given id is (will be) assigned to
     * @throws NoSuchElementException      if project or user with given id does not exist
     * @throws UnauthorizedAccessException if logged user has no privileges to add user to project
     */
    Project addUserToProject(long userId, long projectId) throws NoSuchElementException, UnauthorizedAccessException;

    /**
     * @param userId    User id
     * @param projectId Project id
     * @return number of affected rows in DB
     * @throws UnauthorizedAccessException if logged user has no privileges to delete user from project
     */
    boolean deleteUserFromProject(long userId, long projectId) throws UnauthorizedAccessException;

    /**
     * @param project object
     * @return saved / updated object
     * @throws NoSuchElementException      if project with given id is not in database
     * @throws NullPointerException        if project param is null or project name is not given
     * @throws UnauthorizedAccessException if logged user has no privileges to save / update project
     */
    Project save(Project project) throws NoSuchElementException, NullPointerException, UnauthorizedAccessException;

    /**
     * @return List of all Projects from DB
     */

    List<Project> getAll();


    /**
     * @param id project id
     * @return true / false if delete was successful
     * @throws NoSuchElementException      if project with given id is not in DB
     * @throws UnauthorizedAccessException if logged user has no privileges to delete project
     */

    boolean delete(long id) throws NoSuchElementException, UnauthorizedAccessException;
}
