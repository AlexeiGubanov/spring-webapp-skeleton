package org.swas.dao.hibernate;

import org.swas.dao.hibernate.util.ListOrderAdapter;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.swas.dao.GenericDao;
import org.swas.dao.util.ListOrder;
import org.swas.dao.util.ListParams;
import org.swas.dao.util.PagedList;

import java.io.Serializable;
import java.util.List;

/**
 * Hibernate <code>GenericDao</code> implementation.
 *
 * @param <T>  the type of the domain object
 * @param <ID> the type of the id of domain object
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    @Autowired
    protected SessionFactory sessionFactory;

    private Class<T> persistentClass;

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    public GenericDaoImpl(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public GenericDaoImpl(Class<T> persistentClass, SessionFactory sessionFactory) {
        this(persistentClass);
        this.sessionFactory = sessionFactory;
    }


    @SuppressWarnings("unchecked")
    public ID create(Object entity) {
        return (ID) sessionFactory.getCurrentSession().save(entity);

    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void update(T entity) {
        sessionFactory.getCurrentSession().update(entity);

    }

    public void delete(T entity) {
        sessionFactory.getCurrentSession().delete(entity);

    }

    public void createOrUpdate(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public Object merge(T entity) {
        return sessionFactory.getCurrentSession().merge(entity);
    }

    @SuppressWarnings("unchecked")
    public T get(ID id) {
        return (T) sessionFactory.getCurrentSession().get(persistentClass, id);

    }

    public void refresh(T... entities) {
        for (T entity : entities)
            sessionFactory.getCurrentSession().refresh(entity);

    }

    public List<T> findAll() {
        return findAll(ListParams.EMPTY);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll(ListParams params) {
        return getResultList(getDetachedCriteria(), params);
    }

    protected DetachedCriteria getDetachedCriteria() {
        return DetachedCriteria.forClass(getPersistentClass());
    }


    @SuppressWarnings("unchecked")
    protected List getResultList(Criteria criteria, ListParams params) {
        List result;
        if (!ListParams.EMPTY.equals(params)) {
            if (params.isUsePagination()) {
                long totalCount = getTotalCount(criteria);
                result = new PagedList(applyListParams(criteria, params).list(), totalCount);
            } else {
                result = applyListParams(criteria, params).list();
            }
        } else {
            result = criteria.list();
        }
        return result;

    }

    protected List getResultList(DetachedCriteria criteria, ListParams params) {
        return getResultList(getExecutableCriteria(criteria), params);
    }

    protected Criteria getExecutableCriteria(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(sessionFactory.getCurrentSession());
    }

//    @SuppressWarnings("unchecked")
//    public List<T> findByExample(T example, String... excludeProperty) {
//        Example e = Example.create(example);
//        for (String property : excludeProperty) {
//            e.excludeProperty(property);
//        }
//        DetachedCriteria c = getDetachedCriteria();
//        c.add(e);
//        return getResultList(c, ListParams.EMPTY);
//    }

    public static long getTotalCount(Criteria criteria) {
        //TODO try to clone criteria (if possible)
        Projection p = null;
        ResultTransformer rt = Criteria.ROOT_ENTITY;
        if (criteria instanceof CriteriaImpl) {
            p = ((CriteriaImpl) criteria).getProjection();
            rt = ((CriteriaImpl) criteria).getResultTransformer();
        }
        criteria.setProjection(Projections.rowCount());
        long totalCount = (Long) criteria.uniqueResult();
        criteria.setProjection(p);
        criteria.setResultTransformer(rt);

        return totalCount;
    }


    public static Criteria applyListParams(Criteria criteria, ListParams params) {
        criteria.setFirstResult(params.getOffset());
        criteria.setMaxResults(params.getCount());
        applyListOrders(criteria, params.getOrders());
        return criteria;
    }

    public static void applyListOrders(Criteria criteria, ListOrder[] orders) {
        if (orders == null)
            return;
        for (ListOrder lo : orders) {
            criteria.addOrder(ListOrderAdapter.getHibernateOrder(lo));
        }
    }

    public static void applyListOrders(DetachedCriteria criteria, ListOrder[] orders) {
        if (orders == null)
            return;
        for (ListOrder lo : orders) {
            criteria.addOrder(ListOrderAdapter.getHibernateOrder(lo));
        }
    }

    public Long count() {
        return getTotalCount(getExecutableCriteria(getDetachedCriteria()));
    }


}
