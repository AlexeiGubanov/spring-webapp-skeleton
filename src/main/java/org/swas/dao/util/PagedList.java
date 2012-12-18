package org.swas.dao.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Paged functionality decorator for {@link List} instances. <br/>
 * Adds {@link #getTotalCount()}  param.
 *
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
@SuppressWarnings({"unchecked"})
public class PagedList<T> implements List<T>, Serializable {

    private static final long serialVersionUID = -5820655628956173929L;

    protected List<T> list;

    protected long totalCount;

    /**
     * @return source list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * @param list source list
     */
    public PagedList(List<T> list) {
        this.list = list;
    }

    /**
     * @param list       source list
     * @param totalCount total count of records in database resultset
     */
    public PagedList(List<T> list, long totalCount) {
        this(list);
        this.totalCount = totalCount;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }


    public Iterator iterator() {
        return list.iterator();
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    public boolean add(T o) {
        return list.add(o);
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public boolean containsAll(Collection collection) {
        return list.containsAll(collection);
    }

    public boolean addAll(Collection collection) {
        return list.addAll(collection);
    }

    public boolean addAll(int i, Collection collection) {
        return list.addAll(i, collection);
    }

    public boolean removeAll(Collection collection) {
        return list.removeAll(collection);
    }

    public boolean retainAll(Collection collection) {
        return list.retainAll(collection);
    }

    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    public T get(int i) {
        return list.get(i);
    }

    public T set(int i, T o) {
        return list.set(i, o);
    }

    public void add(int i, T o) {
        list.add(i, o);
    }

    public T remove(int i) {
        return list.remove(i);
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return list.listIterator();
    }

    public ListIterator listIterator(int i) {
        return list.listIterator(i);
    }

    public List subList(int i, int i1) {
        return list.subList(i, i1);
    }

    /**
     * @return total count of records in database resultset
     */
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
}

