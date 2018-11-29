package cn.itcast.core.service.order;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private IdWorker idWorker;

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private PayLogDao payLogDao;

    @Resource
    private ItemDao itemDao;

    @Override
    public void add(Order order) {

        //1.从redis中取出购物车列表
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());
        if (cartList==null){
            return;
        }

        //判断库存是否充足
        for (Cart cart : cartList) {
            for (OrderItem orderItem : cart.getOrderItemList()) {
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                Integer stockCount = item.getStockCount();
                if (stockCount==null){
                    stockCount=0;
                }
                if (item.getNum()>=orderItem.getNum()){
                    item.setStockCount(stockCount +orderItem.getNum());
                    item.setNum(item.getNum()-orderItem.getNum());
                    itemDao.updateByPrimaryKey(item);
                }else {
                    throw new RuntimeException("库存不足");
                }
            }
        }

        String outTradeNo = idWorker.nextId()+"";

        long totalMoney=0;
        //2.添加订单
        for (Cart cart : cartList) {
            Order tbOrder=new Order();
            long orderId = idWorker.nextId();
            tbOrder.setOrderId(orderId);
            tbOrder.setPaymentType(order.getPaymentType());
            tbOrder.setStatus("1");
            tbOrder.setCreateTime(new Date());
            tbOrder.setReceiverAreaName(order.getReceiverAreaName());
            tbOrder.setReceiverMobile(order.getReceiverMobile());
            tbOrder.setReceiver(order.getReceiver());
            tbOrder.setSourceType(order.getSourceType());
            tbOrder.setSellerId(cart.getSellerId());
            tbOrder.setOutTradeNo(outTradeNo);


            double money=0;
            for (OrderItem orderItem : cart.getOrderItemList()) {
                orderItem.setId(idWorker.nextId());
                orderItem.setOrderId(orderId);
                orderItemDao.insert(orderItem);
                money+=orderItem.getTotalFee().doubleValue();
            }

            totalMoney+=(long)(money*100);
            //实付金额
            tbOrder.setPayment(new BigDecimal(money));
            orderDao.insert(tbOrder);

        }

        //3.如果是微信支付,需添加支付日志
        if ("1".equals(order.getPaymentType())){
            PayLog payLog=new PayLog();
            payLog.setOutTradeNo(outTradeNo);
            payLog.setCreateTime(new Date());
            payLog.setTotalFee(totalMoney);
            payLog.setUserId(order.getUserId());
            payLog.setTradeState("0");
            payLog.setPayType("1");
            payLogDao.insert(payLog);

            redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog);
        }



        //4.删除购物车列表
        redisTemplate.boundHashOps("cartList").delete(order.getUserId());
    }

    @Override
    public PayLog searchPayLogFromRedis(String userId) {
        return (PayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

    @Override
    public void updateOrderStatus(String out_trade_no, String transaction_id) {
        //修改日志信息
        PayLog payLog = payLogDao.selectByPrimaryKey(out_trade_no);
        payLog.setTradeState("1");
        payLog.setPayTime(new Date());
        payLog.setTransactionId(transaction_id);
        payLogDao.updateByPrimaryKey(payLog);

        //修改订单状态
        OrderQuery query=new OrderQuery();
        query.createCriteria().andOutTradeNoEqualTo(out_trade_no);
        Order order=new Order();
        order.setStatus("2");
        order.setPaymentTime(new Date());
        orderDao.updateByExampleSelective(order,query);

        //删除redis中的payLog信息
        redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
    }
	
	@Override
    public List<Order> findAll() {
        return orderDao.selectByExample(null);
    }
}
