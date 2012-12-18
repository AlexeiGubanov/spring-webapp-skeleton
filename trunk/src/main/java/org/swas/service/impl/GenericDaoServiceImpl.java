package org.swas.service.impl;

import org.swas.dao.GenericDao;
import org.swas.dao.util.ListParams;
import org.swas.domain.Persistable;
import org.swas.service.GenericDaoService;
import org.swas.service.Result;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public class GenericDaoServiceImpl<T extends Persistable<ID>, ID extends Serializable> implements GenericDaoService<T, ID> {


    protected GenericDao<T, ID> dao;

    protected GenericDao<T, ID> getDao() {
        return dao;
    }

    public GenericDaoServiceImpl(GenericDao<T, ID> dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public T get(ID id) {
        if (id == null)
            return null;
        return getDao().get(id);
    }

    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Transactional(readOnly = true)
    public List<T> findAll(ListParams params) {
        return getDao().findAll(params);
    }

    @Transactional(readOnly = true)
    public void refresh(T... entities) {
        for (T e : entities) {
            getDao().refresh(e);
        }
    }

    @Transactional
    public void delete(ID... ids) {
        for (ID id : ids) {
            T e = getDao().get(id);
            if (e != null)
                getDao().delete(e);
        }
    }

    @Transactional
    public void delete(T... entities) {
        for (T e : entities) {
            getDao().delete(e);
        }
    }

//    @Transactional
//    public Result createOrUpdate(T... entities) {
//        List<ID> ids = new ArrayList<ID>();
//        for (T e : entities) {
//            dao.createOrUpdate(e);
//            ids.add(e.getId());
//        }
//        return new Result(ids);
//    }

    @Transactional
    public Result createOrUpdate(T... entities) {
        List<T> r = new ArrayList<T>();
        for (T e : entities) {
            //TODO по хорошему тут нужно делать merge, т.к. возможно не все поля придут и некоторые затрутся
            getDao().createOrUpdate(e);
            r.add(e);
        }
        return new Result(r);
    }

    @Transactional
    public Result create(T... entities) {
        List<T> r = new ArrayList<T>();
        for (T e : entities) {
            //TODO по хорошему тут нужно делать merge, т.к. возможно не все поля придут и некоторые затрутся
            getDao().create(e);
            r.add(e);
        }
        return new Result(r);
    }
}
