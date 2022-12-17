package sk.upjs.dao;

import sk.upjs.entity.Bug;
import sk.upjs.entity.Severity;
import sk.upjs.entity.Status;

import java.util.List;
import java.util.NoSuchElementException;

public interface BugDao {

    /**
     * @param id of bug
     * @return Bug from database
     */
    Bug getById(long id);

    /**
     * @return List of Bugs from database
     */
    List<Bug> getAll();

    /**
     * @param bug object
     * @return saved / updated Bug
     * @throws NoSuchElementException      id of bug is negative or bug with given id is not in database
     * @throws NullPointerException        if bug object is null or
     * @throws UnauthorizedAccessException if logged user is has no privileges to save or update
     */

    Bug save(Bug bug) throws NoSuchElementException, NullPointerException, UnauthorizedAccessException;

    /**
     * @param bugId    bug id
     * @param statusId status id
     * @return Bug object with changed status
     * @throws NoSuchElementException if statusId is not in database or bug with given id is not in database
     */

    Bug changeStatus(long bugId, long statusId) throws NoSuchElementException;

    /**
     * @param id Bug id
     * @return true / false if it was successful
     * @throws UnauthorizedAccessException if logged user is has no privileges delete from database
     */
    boolean delete(long id) throws UnauthorizedAccessException;

    /**
     * @return list of all Statuses from database
     */
    List<Status> getAllStatuses();

    /**
     * @return list of all Severities from database
     */

    List<Severity> getAllSeverities();

    /**
     * @param id severity id
     * @return Severity object
     */
    Severity getSeverityById(long id);

    /**
     * @param id status id
     * @return Status object
     */

    Status getStatusById(long id);
}
