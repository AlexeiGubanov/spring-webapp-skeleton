package org.swas.web.dto.params;

import org.swas.dao.util.ListOrder;
import org.swas.dao.util.ListParams;

import java.util.Map;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 25.11.11
 */
public class ListRequestParams {

    private static final Long maxPerRequest = 1000l;

    private Long offset;

    private Long limit;

    private String sort;

    private String dir;

    private ListOrder[] orderList;

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public ListOrder[] getOrderList() {
        if (orderList == null) {
            this.orderList = ordersFromString(this.sort, this.dir);
        }
        return orderList;
    }

    public void setOrderList(ListOrder[] orderList) {
        this.orderList = orderList;
    }

    public ListRequestParams() {
    }

    protected Long getDefaultLimit() {
        return maxPerRequest;
    }

    /**
     * Заполняет начальными данными поля offset и limit если явно не задано
     */
    public void fillUpParamsDefault() {
        offset = (offset == null) ? 0 : offset;
        Long defaultLimit = getDefaultLimit();
        limit = (limit == null || limit > defaultLimit) ? defaultLimit : limit;
    }

    @Deprecated
    public static ListParams buildListParams(ListRequestParams params) {
        params.fillUpParamsDefault();
        ListParams lp = new ListParams();
        lp.setCount(params.getLimit().intValue());
        lp.setOffset(params.getOffset().intValue());
        lp.setOrders(params.getOrderList());
        return lp;
    }

    public ListParams asListParams() {
        this.fillUpParamsDefault();
        ListParams lp = new ListParams();
        lp.setCount(this.getLimit().intValue());
        lp.setOffset(this.getOffset().intValue());
        lp.setOrders(this.getOrderList());
        return lp;
    }

    public static ListOrder[] ordersFromString(String names, String directions) {
        if (names == null) {
            return null;
        }
        String[] namesList = names.split(",");
        boolean[] descendings = new boolean[namesList.length];
        ListOrder[] listOrders = new ListOrder[namesList.length];
        if (directions != null) {
            String[] directionsList = directions.split(",");
            for (int i = 0; i < directionsList.length && i < namesList.length; i++) {
                if (directionsList[i].charAt(0) == 'D'
                        || directionsList[i].charAt(0) == 'd'
                        || directionsList[i].charAt(0) == '0') {
                    descendings[i] = true;
                }
            }
        }
        for (int i = 0; i < namesList.length; i++) {
            listOrders[i] = new ListOrder(namesList[i], !descendings[i]);
        }
        return listOrders;
    }

    public static void bindPager(Map<String, Object> model, Long total, ListParams lp) {
        model.put("total", total);
        model.put("params", lp);

    }


}
