package org.swas.dao;

import org.swas.dao.util.ListParams;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for DAO for a single domain object.
 *
 * @param <T>  the type of the domain object
 * @param <ID> the type of the id of domain object
 * @author Alexei.Gubanov@gmail.com
 * @see com.googlecode.genericdao.dao.hibernate.GenericDAO
 */
public interface GenericDao<T, ID extends Serializable> {

    /**
     * <p/>
     * Get the entity with the appropriate type and id from the datasource.
     * <p/>
     * If none is found, return null.
     */
    T get(ID id);

    /**
     * <p/>
     * Save the entity in datasource.
     * <p/>
     * Returns new id of the entity.
     */
    ID create(T entity);

    /**
     * <p/>
     * Update entity in datasource.
     */
    void update(T entity);

    /**
     * <p/>
     * Create or update entity in datasource.
     */
    void createOrUpdate(T entity);

    /**
     * <p/>
     * Remove entity from datasource
     */
    void delete(T entity);

    /**
     * <p/>
     * Refresh given entity with datasource state
     */
    void refresh(T... entities);

    /**
     * <p/>
     * Find all objects in datasource for appropriate domain object
     */
    List<T> findAll();

    /**
     * <p/>
     * Find all objects in datasource for appropriate domain object with specified <code>ListParams</code> object.
     */
    List<T> findAll(ListParams params);

    /**
     * <p/>
     * Get total records count in datasource for appropriate domain object.
     *
     */
    Long count();
}
