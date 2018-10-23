package cz.fi.muni.pa165.secretagency.dao;

import java.util.List;

/**
 * Interface which describes common operations over entities.
 * Every other DAO interface should extend this one.
 *
 * @author Jan Pavlu (487548)
 *
 * @param <T> Entity
 */
public interface GenericDao<T> {

    /**
     * Persists entity into database.
     * @param t Entity which is going to be persisted
     * @return Saved entity.
     */
    T save(T t);

    /**
     * Deletes entity from database.
     * @param t Entity which is going to be deleted.
     */
    void delete(T t);

    /**
     * Deletes entity from database.
     * @param id Id of entity which is going to be deleted.
     */
    void deleteEntityById(Long id);

    /**
     * Returns entity with given id.
     * @param id Id of entity.
     * @return Entity with given id.
     */
    T getEntityById(Long id);

    /**
     * Returns  all entities.
     * @return All entities from table.
     */
    List<T> getAll();
}
