package org.swas.dao.hibernate.util;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.swas.domain.BasicPersistent;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

/**
 * Common interceptor for hibernata dao implementation.
 * Contains logic for {@link BasicPersistent} childs:
 * <ul>
 *  <li>set fantomId to null after successfull flush</li>
 *  <li>isTransient override (set id to null, so id must be regenerated on save).</li>
 *  <li>lastUpdate property update before flushing</li>
 *  </ul>
 * @author Alexei.Gubanov@gmail.com
 *         Date: 19.01.12
 */
public class CommonInterceptor extends EmptyInterceptor {

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (entity instanceof BasicPersistent) {
            BasicPersistent oe = (BasicPersistent) entity;
            oe.setLastUpdate(new Date());
        }
        return false;
    }

    @Override
    public void postFlush(Iterator entities) {
        while (entities.hasNext()) {
            Object e = entities.next();
            if (e instanceof BasicPersistent) {
                BasicPersistent oe = (BasicPersistent) e;
                if (oe.isFantom())
                    oe.setFantomId(null);
            }
        }
    }

    @Override
    public Boolean isTransient(Object entity) {
        if (entity instanceof BasicPersistent) {
            BasicPersistent oe = (BasicPersistent) entity;
            if (oe.isFantom()) {
                oe.setId(null);
                return Boolean.TRUE;
            }
        }
        return null;
    }
}