package sk.upjs.dao;

import sk.upjs.entity.Bug;

import java.util.List;
import java.util.NoSuchElementException;

public interface BugDao {
    Bug getById(long id) throws NoSuchElementException;

    List<Bug> getAll();

    Bug save(Bug bug) throws NoSuchElementException, NullPointerException;

    Bug changeStatus(Bug bug, long statusId) throws NoSuchElementException;

    boolean delete(long id);
}
