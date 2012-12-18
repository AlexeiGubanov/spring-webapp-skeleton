package org.swas.dao.util;

/**
 * Represents an order param for data requests
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public class ListOrder {

    private String propertyName;
    private boolean ascending;

    /**
     * @return property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @return true if ascending
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * Basic contructor
     *
     * @param propertyName property name
     * @param ascending    true for ascending
     */
    public ListOrder(String propertyName, boolean ascending) {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    /**
     * Ascending order
     *
     * @param propertyName property name
     * @return {@link ListOrder} instance
     */
    public static ListOrder asc(String propertyName) {
        return new ListOrder(propertyName, true);
    }

    /**
     * Descending order
     *
     * @param propertyName property name
     * @return {@link ListOrder} instance
     */
    public static ListOrder desc(String propertyName) {
        return new ListOrder(propertyName, false);
    }
}
