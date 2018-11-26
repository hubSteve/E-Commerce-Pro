package cn.itcast.core.service.seckill;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {


    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SeckillGoodsDao seckillGoodsDao;

    @Resource
    private SeckillOrderDao seckillOrderDao;

    @Override
    @Transactional
    public void submitOrder(Long seckillId, String userId) {

        //一个用户只能一次完成一次秒杀
        if (redisTemplate.boundHashOps("seckillOrder").get(userId)!=null){
            throw new RuntimeException("请完成上一个订单");
        }

        //获取商品信息
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillId);
        if (seckillGoods==null || seckillGoods.getNum()<=0){
            throw new RuntimeException("商品已抢购一空");
        }

        //扣减库存
        seckillGoods.setStockCount(seckillGoods.getStockCount()+1);
        seckillGoods.setNum(seckillGoods.getNum()-1);
        redisTemplate.boundHashOps("seckillGoods").put(seckillId,seckillGoods);

        if (seckillGoods.getNum()==0){
            seckillGoodsDao.updateByPrimaryKey(seckillGoods);
            redisTemplate.boundHashOps("seckillGoods").delete(seckillId);
        }

        SeckillOrder seckillOrder=new SeckillOrder();
        IdWorker idWorker=new IdWorker();
        seckillOrder.setId(idWorker.nextId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setMoney(seckillGoods.getCostPrice());
        seckillOrder.setSeckillId(Long.valueOf(seckillId));
        seckillOrder.setSellerId(seckillGoods.getSellerId());
        seckillOrder.setUserId(userId);
        seckillOrder.setStatus("0");
        redisTemplate.boundHashOps("seckillOrder").put(userId,seckillOrder);

    }

    @Override
    public SeckillOrder searchOrderFromRedis(String userId) {
        return (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
    }

    @Override
    @Transactional
    public void saveOrderFromRedis(String userId, Long orderId, String transactionId) {
        SeckillOrder seckillOrder = searchOrderFromRedis(userId);
        if (seckillOrder==null){
            throw new RuntimeException("订单不存在");
        }
        if (seckillOrder.getId().longValue()!=orderId.longValue()){
            throw new RuntimeException("订单不相符");
        }
        //设置属性
        seckillOrder.setStatus("1");
        seckillOrder.setPayTime(new Date());
        seckillOrder.setTransactionId(transactionId);
        seckillOrderDao.insert(seckillOrder);

        //清空redis
        redisTemplate.boundHashOps("seckillOrder").delete(userId);
    }

    @Override
    public void deleteOrderFromRedis(String userId, Long orderId) {
        //取出redis中的订单
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("seckillOrder").get(userId);
        if (seckillOrder!=null && seckillOrder.getId().longValue()==orderId.longValue()){
            //修改库存
            SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(seckillOrder.getSeckillId());
            if (seckillGoods!=null){
                seckillGoods.setNum(seckillGoods.getNum()+1);
                seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
                redisTemplate.boundHashOps("seckillGoods").put(seckillOrder.getSeckillId(),seckillGoods);
            }else {
                seckillGoods=seckillGoodsDao.selectByPrimaryKey(seckillOrder.getSeckillId());
                if (seckillGoods!=null) {
                    seckillGoods.setNum(seckillGoods.getNum() + 1);
                    seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
                    seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
                }
            }
            //删除订单
            redisTemplate.boundHashOps("seckillOrder").delete(userId);
        }

    }
}
