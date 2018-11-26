package cn.itcast.core.service.goods;

import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.vo.GoodsVo;
import com.github.pagehelper.PageInfo;

public interface GoodsService {

    void add(GoodsVo goodsVo);

    PageInfo<Goods> search(Integer page,Integer rows,Goods goods);

    GoodsVo findOne(Long id);

    void update(GoodsVo goodsVo);

    PageInfo<Goods> searchForManager(Integer page,Integer rows,Goods goods);

    void updateStatus(long[] ids,String status);

    void delete(long[] ids);
}
