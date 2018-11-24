package com.its.core.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作用：分页对象，封装页面需要的数据
 */
public class PageResult implements Serializable{

    private Long total;     // 总条数
    private List rows;      // 结果集

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
