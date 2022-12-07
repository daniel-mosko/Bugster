package sk.upjs.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.Project;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProjectDao {
    Project getById(long id) throws NoSuchElementException, EmptyResultDataAccessException;

    List<Project> getByUserId(long id) throws NoSuchElementException;

    Project addUserToProject(long userId, long projectId) throws NoSuchElementException, UnauthorizedAccessException;

    boolean deleteUserFromProject(long userId, long projectId) throws UnauthorizedAccessException;

    Project save(Project project) throws NoSuchElementException, NullPointerException,UnauthorizedAccessException;

    List<Project> getAll();

    boolean delete(long id);
}
