package cn.itcast.core.service.order;

import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;

import java.util.List;

public interface OrderService {

    void add(Order order);

    PayLog searchPayLogFromRedis(String userId);

    void updateOrderStatus(String out_trade_no,String transaction_id);

    List<Order> findAll();
}
