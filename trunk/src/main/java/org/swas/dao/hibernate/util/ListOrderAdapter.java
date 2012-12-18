package org.swas.dao.hibernate.util;

import org.hibernate.criterion.Order;
import org.swas.dao.util.ListOrder;

/**
 * Adapter that converts {@link ListOrder} to {@link Order}
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public class ListOrderAdapter {

    public static Order getHibernateOrder(ListOrder order) {
        if (order != null) {
            if (order.isAscending()) {
                return Order.asc(order.getPropertyName());
            } else {
                return Order.desc(order.getPropertyName());
            }
        }
        return null;
    }
}
