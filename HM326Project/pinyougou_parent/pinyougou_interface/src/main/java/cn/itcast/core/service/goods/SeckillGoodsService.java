package cn.itcast.core.service.goods;

import cn.itcast.core.pojo.good.Goods;

public interface SeckillGoodsService {

    /** 秒杀商品申请*/
    public void add(Goods goods);
}
