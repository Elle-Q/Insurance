package com.fintech.insurance.commons.utils;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {

    // 当前页数
    private long pageIndex;

    // 单页大小
    private long pageSize;

    // 总记录数
    private long totalRowsCount;

    // 总页数
    private long pagesCount;

    private List<T> items;

    public Pagination(long pageIndex, long pageSize, long totalRowsCount, List<T> items) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalRowsCount = totalRowsCount;
        this.pagesCount = totalRowsCount % pageSize == 0 ? totalRowsCount / pageSize : (totalRowsCount / pageSize + 1);
        this.items = null == items ? new ArrayList<T>() : items;
    }

    public Pagination(){};

    public static <T> Pagination<T> createInstance(Number pageIndex, Number pageSize, final Number totalRowsCount, List<T> data) {
        Pagination<T> pagination = new Pagination<T>(pageIndex.longValue(), pageSize.longValue(),
                totalRowsCount.longValue(), data);
        return pagination;
    }

    public long getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(long pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRowsCount() {
        return totalRowsCount;
    }

    public void setTotalRowsCount(long totalRowsCount) {
        this.totalRowsCount = totalRowsCount;
    }

    public long getPagesCount() {
        return pagesCount;
    }

    public void setPagesCount(long pagesCount) {
        this.pagesCount = pagesCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
