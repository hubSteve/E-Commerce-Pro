package cn.itcast.core.service.itemCat;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    List<ItemCat> findByParentId(long parentId);

    void add(ItemCat itemCat);

    ItemCat findOne(long id);

    List<ItemCat> findAll();

    //查询所有的statsu==0的商品分类
    PageResult searchItemCatListByStatus (Integer page,Integer rows,ItemCat itemCat);
    //审核
    void updateStatus(long[]ids,String status);
}
