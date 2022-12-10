package sk.upjs.dao;

import sk.upjs.entity.Bug;
import sk.upjs.entity.Severity;
import sk.upjs.entity.Status;

import java.util.List;
import java.util.NoSuchElementException;

public interface BugDao {
    Bug getById(long id) throws NoSuchElementException;

    List<Bug> getAll();

    Bug save(Bug bug) throws NoSuchElementException, NullPointerException, UnauthorizedAccessException;

    Bug changeStatus(Bug bug, long statusId) throws NoSuchElementException;

    boolean delete(long id) throws UnauthorizedAccessException;

    List<Status> getAllStatuses();

    List<Severity> getAllSeverities();
    Severity getSeverityById(long id);
    Status getStatusById(long id);
}
