package cn.itcast.core.service.orders;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

/**
 * 运营商后台 订单
 */
public interface OrdersQueryService {
    public PageResult OrdersQuery(Integer pageNum, Integer pageSize, Order order);
}
