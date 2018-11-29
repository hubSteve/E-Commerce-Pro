package cn.itcast.core.service.order;

import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


import javax.annotation.Resource;
import java.util.List;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Resource
    private SeckillOrderDao seckillOrderDao;

    @Override
    public PageInfo<SeckillOrder> search(Integer page,Integer rows,SeckillOrder seckillOrder) {
        // 设置分页条件
        PageHelper.startPage(page, rows);
        // 设置查询条件
        SeckillOrderQuery query = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria criteria = query.createCriteria();
        if (seckillOrder.getSellerId()!=null && !"".equals(seckillOrder.getSellerId().trim())){
            criteria.andSellerIdEqualTo(seckillOrder.getSellerId().trim());
        }
        criteria.andStatusEqualTo("1");

        query.setOrderByClause("create_time desc");// 降序
        // 查询
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(query);
        PageInfo<SeckillOrder> pageInfo=new PageInfo<>(seckillOrders);
        return pageInfo;
    }

    /** 运营商查询秒杀订单*/
    @Override
    public PageInfo<SeckillOrder> searchForManager(Integer page,Integer rows,SeckillOrder seckillOrder) {

        // 设置分页条件
        PageHelper.startPage(page, rows);
        // 设置查询条件
        SeckillOrderQuery query = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria criteria = query.createCriteria();

        if (seckillOrder.getStatus()!=null && !"".equals(seckillOrder.getStatus().trim())){
            criteria.andStatusEqualTo(seckillOrder.getStatus().trim());
        }
        query.setOrderByClause("create_time desc");// 降序
        // 查询
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(query);
        PageInfo<SeckillOrder> pageInfo=new PageInfo<>(seckillOrders);
        return pageInfo;
    }

}

