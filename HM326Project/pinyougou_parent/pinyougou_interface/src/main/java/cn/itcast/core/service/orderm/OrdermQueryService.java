package cn.itcast.core.service.orderm;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;


public interface OrdermQueryService {
    /**商家后台查询
     * @param pageNum
     * @param pageSize
     * @param order
     * @return
     */
    public PageResult OrdermQuery(Integer pageNum, Integer pageSize, Order order);

  /*  public PageResult Orderf(Integer pageNum, Integer pageSize, Order order);*/
}
