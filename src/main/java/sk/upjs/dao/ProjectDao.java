package sk.upjs.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import sk.upjs.entity.Project;

import java.util.List;
import java.util.NoSuchElementException;

public interface ProjectDao {
    Project getById(long id) throws NoSuchElementException, EmptyResultDataAccessException;

    List<Project> getByUser(long id);

    Project save(Project project) throws NoSuchElementException, NullPointerException;

    List<Project> getAll();

    boolean delete(long id);
}
