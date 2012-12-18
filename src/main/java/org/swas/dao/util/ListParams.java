package org.swas.dao.util;

/**
 * Represents data request params.
 * Contains: <ul>
 * <li>offset - offset to first result from datasource resultset</li>
 * <li>count - max resultset records count</li>
 * <li>usePagination - true if target resultset must be {@link PagedList}</li>
 * <li>orders - array of {@link ListOrder}</li>
 * </ul>
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
public class ListParams {

    private int offset;
    private int count;
    private boolean usePagination;
    private ListOrder[] orders;

    /**
     * Empty (implicity) params
     */
    public static final ListParams EMPTY = new ListParams(0, Integer.MAX_VALUE, false);


    /**
     * List params with list order params only
     *
     * @param orders list order params
     */
    public ListParams(ListOrder... orders) {
        this(0, Integer.MAX_VALUE, false, orders);
    }

    /**
     * List params with pagination params only, {@link #isUsePagination()} is true.
     *
     * @param offset offset to first result
     * @param count  max resultset records count
     */
    public ListParams(int offset, int count) {
        this(offset, count, true);
    }

    /**
     * List params with orders and pagination,  {@link #isUsePagination()} is true.
     *
     * @param offset offset to first result
     * @param count  max resultset records count
     * @param orders list order params
     */
    public ListParams(int offset, int count, ListOrder... orders) {
        this(offset, count, true, orders);
    }

    /**
     * Universal contructor
     *
     * @param offset        offset to first result
     * @param count         max resultset records count
     * @param usePagination true if target resultset must be {@link PagedList}
     * @param orders        list order params
     */
    public ListParams(int offset, int count, boolean usePagination, ListOrder... orders) {
        this.offset = offset;
        this.count = count;
        this.usePagination = usePagination;
        this.orders = orders;
    }

    /**
     * @return offset to first result
     */
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return max resultset records count
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return true if target resultset must be {@link PagedList}
     */
    public boolean isUsePagination() {
        return usePagination;
    }

    public void setUsePagination(boolean usePagination) {
        this.usePagination = usePagination;
    }

    /**
     * @return list order params
     */
    public ListOrder[] getOrders() {
        return orders;
    }

    public void setOrders(ListOrder[] orders) {
        this.orders = orders;
    }
}
