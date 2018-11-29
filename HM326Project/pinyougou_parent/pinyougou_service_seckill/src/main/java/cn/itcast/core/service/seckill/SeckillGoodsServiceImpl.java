package cn.itcast.core.service.seckill;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
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
     * 如果过时,则删除,反之则保留
     *
     * @return
     */
    @Override
    public List<SeckillGoods> findList() {

        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        if (seckillGoodsList == null || seckillGoodsList.size() == 0) {
            SeckillGoodsQuery query = new SeckillGoodsQuery();
            SeckillGoodsQuery.Criteria criteria = query.createCriteria();
            criteria.andStatusEqualTo("1");
            criteria.andNumGreaterThan(0);
            criteria.andStartTimeLessThanOrEqualTo(new Date());
            criteria.andEndTimeGreaterThan(new Date());

            seckillGoodsList = seckillGoodsDao.selectByExample(query);
            if (seckillGoodsList != null && seckillGoodsList.size() > 0) {
                for (SeckillGoods seckillGoods : seckillGoodsList) {
                    redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(), seckillGoods);
                }
            }
            return seckillGoodsList;
        }else{
                System.out.println("从缓存中取出记录");
                //redis里某些商品已经过期,如果长期不清理掉,没有任何意义,反而会占据空间
                for (SeckillGoods seckillGoods : seckillGoodsList) {
                    Date endTime = seckillGoods.getEndTime();
                    if (endTime.compareTo(new Date()) == -1 || seckillGoods.getNum() == 0) {
                        redisTemplate.boundHashOps("seckillGoods").delete(seckillGoods.getId());
                        seckillGoodsList.remove(seckillGoods);
                    }
                }
            return seckillGoodsList;
            }
        }

    @Override
    public SeckillGoods findOneFromRedis(Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(id);
    }


    /**
     * 秒杀商品增量同步
     *   秒杀考虑到有一种例外的情况,就是商家提交申请的秒杀商品开
     *   始时间大于当前时间,并且结束时间也大于当前时间,这时如果
     *   使用SpringTask来实现单独一个数据库增量同步就会出现漏查
     *   数据的可能,但是我们使用的秒杀页面只有一个,因此不会出现
     *   多个秒杀页面要实现的功能
     */
    @Scheduled(cron = "* 0/10 * * * *")//要执行的时间 每间隔10分钟执行一次
    private void redisSynchronizeFromDb() {
        System.out.println("+++++++++++++++++++++定时任务执行了++++++++++++++++++++++");
        SeckillGoodsQuery skgQuery = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = skgQuery.createCriteria();
        criteria.andStartTimeLessThanOrEqualTo(new Date());
        criteria.andEndTimeGreaterThan(new Date());
        criteria.andNumGreaterThan(0);
        criteria.andStatusEqualTo("1");
        //获取到集合
        List<SeckillGoods> latestSeckillGoodsList = seckillGoodsDao.selectByExample(skgQuery);
        redisTemplate.delete("seckillGoods");
        for (SeckillGoods seckillGoods : latestSeckillGoodsList) {
            redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getId(), seckillGoods);
        }

    }



}
