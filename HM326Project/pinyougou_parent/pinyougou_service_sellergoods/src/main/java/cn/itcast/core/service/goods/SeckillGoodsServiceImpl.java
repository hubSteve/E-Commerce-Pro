package cn.itcast.core.service.goods;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;


@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {
    /** 秒杀商品申请*/

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Autowired
    private ItemDao itemDao;

    @Transactional
    @Override
    public void add(Goods goods) {
        SeckillGoods seckillGoods = new SeckillGoods();

        // 1.保存秒杀商品,设置状态,返回自增主键
        seckillGoods.setStatus("0");//设置审核状态

        Long goodsId = goods.getId();
        seckillGoods.setGoodsId(goodsId);//设置秒杀商品id
        seckillGoods.setSmallPic(goods.getSmallPic());//商品图片
        seckillGoods.setPrice(goods.getPrice());//商品原价
        seckillGoods.setSellerId(goods.getSellerId());//商家ID
        seckillGoods.setCreateTime(new Date());//创建时间

        ItemQuery query=new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> itemList = itemDao.selectByExample(query);
        Item item = itemList.get(0);
        seckillGoods.setTitle(item.getTitle());// 秒杀商品标题
        seckillGoods.setCostPrice(item.getCostPirce());//秒杀价格
        seckillGoods.setNum(item.getNum());//秒杀商品数
        seckillGoods.setStockCount(item.getStockCount());//剩余

        seckillGoodsDao.insertSelective(seckillGoods);
    }

}

