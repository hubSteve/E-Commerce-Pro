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


    /**更新数据
     * @param itemCat
     */
    void update(ItemCat itemCat);

    /**批量删除数据
     * @param ids
     */
    void delete(long[] ids);

    public List<ItemCat> findItemCatList();

}
