package cn.itcast.core.service.itemCat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public List<ItemCat> findByParentId(long parentId) {

        List<ItemCat> itemCats = itemCatDao.selectByExample(null);
        if (itemCats!=null && itemCats.size()>0){
            for (ItemCat itemCat : itemCats) {
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
            }
        }

        ItemCatQuery itemCatQuery=new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(itemCatQuery);
    }

    @Override
    public void add(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    @Override
    public ItemCat findOne(long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }


    @Override
    public List<ItemCat> findItemCatList() {
        List<ItemCat> itemCatList = (List<ItemCat>) redisTemplate.boundHashOps("itemCatList").get("indexItemCat");
        if (itemCatList==null){
            List<ItemCat> itemCatList1 = itemCatDao.findItemCatListByParentId(0L);
            for (ItemCat itemCat1 : itemCatList1) {
                List<ItemCat> itemCatList12 = itemCatDao.findItemCatListByParentId(itemCat1.getId());
                for (ItemCat itemCat2 : itemCatList12) {
                    List<ItemCat> itemCatList3 = itemCatDao.findItemCatListByParentId(itemCat2.getId());
                    itemCat2.setItemCatList(itemCatList3);
                }
                itemCat1.setItemCatList(itemCatList12);
            }
            redisTemplate.boundHashOps("itemCatList").put("indexItemCat",itemCatList1);

            return itemCatList1;
        }
        return itemCatList;
    }
}
