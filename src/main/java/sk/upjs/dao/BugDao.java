package sk.upjs.dao;

import sk.upjs.entity.Bug;

import java.util.List;

public interface BugDao {
    Bug getById(long id);

    List<Bug> getAll();

    Bug save(Bug bug);

    boolean delete(long id);
}
