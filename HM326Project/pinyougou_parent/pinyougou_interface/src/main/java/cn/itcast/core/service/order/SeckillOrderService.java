package cn.itcast.core.service.order;

import cn.itcast.core.pojo.seckill.SeckillOrder;
import com.github.pagehelper.PageInfo;


public interface SeckillOrderService {

    /** 根据商家id查询秒杀订单*/
    public PageInfo<SeckillOrder> search(Integer page,Integer rows,SeckillOrder seckillOrder);


    /** 运营商查询秒杀订单*/
    public PageInfo<SeckillOrder> searchForManager(Integer page,Integer rows,SeckillOrder seckillOrder);
}
