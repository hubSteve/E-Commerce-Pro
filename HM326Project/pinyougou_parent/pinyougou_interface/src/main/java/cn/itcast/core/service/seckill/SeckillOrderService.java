package cn.itcast.core.service.seckill;

import cn.itcast.core.pojo.seckill.SeckillOrder;

public interface SeckillOrderService {

    void submitOrder(Long seckillId,String userId);

    SeckillOrder searchOrderFromRedis(String userId);

    void saveOrderFromRedis(String userId,Long orderId,String transactionId);

    void deleteOrderFromRedis(String userId,Long orderId);
}
