package org.swas.service;

import org.swas.dao.util.ListParams;
import org.swas.domain.Persistable;

import java.io.Serializable;
import java.util.List;

/**
 * Basic service interface for dao-aware services.
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 08.12.11
 */
public interface GenericDaoService<T extends Persistable<ID>, ID extends Serializable> {
    T get(ID id);

    void refresh(T... entities);

    List<T> findAll();

    List<T> findAll(ListParams params);

    void delete(ID... ids);

    void delete(T... entities);

    Result createOrUpdate(T... entities);

    Result create(T... entities);

}
