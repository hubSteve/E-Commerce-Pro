package cn.itcast.core.service.itemCat;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;


import java.util.List;

public interface ItemCatService {

    List<ItemCat> findByParentId(long parentId);

    void add(ItemCat itemCat);

    ItemCat findOne(long id);

    List<ItemCat> findAll();

    public List<ItemCat> findItemCatList();

}
