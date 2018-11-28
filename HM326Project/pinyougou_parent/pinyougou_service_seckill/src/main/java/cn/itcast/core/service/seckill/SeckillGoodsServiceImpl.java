package cn.itcast.core.service.seckill;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SeckillGoodsDao seckillGoodsDao;

    /**
     * 下面的方法:主要的任务是先判断redis当中的秒杀数据是否过时
     *            如果过时,则删除,反之则保留
     * @return
     */
    @Override
    public List<SeckillGoods> findList() {

        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        if (seckillGoodsList==null || seckillGoodsList.size()==0){
            SeckillGoodsQuery query=new SeckillGoodsQuery();
            SeckillGoodsQuery.Criteria criteria = query.createCriteria();
            criteria.andStatusEqualTo("1");
            criteria.andNumGreaterThan(0);
            criteria.andStartTimeLessThanOrEqualTo(new Date());
            criteria.andEndTimeGreaterThan(new Date());
            seckillGoodsList = seckillGoodsDao.selectByExample(query);
            if (seckillGoodsList!=null && seckillGoodsList.size()>0){
                for (SeckillGoods seckillGoods : seckillGoodsList) {
                    int result = dateCompare(seckillGoods.getEndTime());
                    //当结束时间大于当前时间 并且 库存不为0 并且状态为审核通过
                    // 才将秒杀数据添加到缓存当中
                    if(result == 1 && seckillGoods.getNum()>0 && seckillGoods.getStatus() =="1"){
                        redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(),seckillGoods);
                    }
               }
            }
        }
        return seckillGoodsList;
    }

    @Override
    public SeckillGoods findOneFromRedis(Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
    }

    private int dateCompare(Date date) {

        return date.compareTo(new Date()); //1 为左面的date大于右面的date,-1为小于,0为等于

    }




}
