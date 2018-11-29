package cn.itcast.core.service.itemCat;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.bouncycastle.util.Arrays;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


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



    //查询所有的statsu==0的商品分类
    @Override
    public PageResult searchItemCatListByStatus(Integer page, Integer rows, ItemCat itemCat) {

        PageHelper.startPage(page,rows);
        ItemCatQuery itemCatQuery=null;

        if(itemCat!=null){
           itemCatQuery=new ItemCatQuery();
            ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();

            if(itemCat.getName()!=null && !"".equals(itemCat.getName().trim())){
            criteria.andNameLike("%"+itemCat.getName().trim()+"%");
           }

           if(itemCat.getStatus()!=null && !"".equals(itemCat.getStatus().trim())){
             criteria.andStatusEqualTo(itemCat.getStatus().trim());
           }

           itemCatQuery.setOrderByClause("id desc");
       }
        Page<ItemCat>page1= (Page<ItemCat>) itemCatDao.selectByExample(itemCatQuery);

        return new PageResult(page1.getTotal(),page1.getResult());
    }

    //审核
    @Override
    @Transactional
    public void updateStatus(long[] ids, String status) {

        if(ids!=null && ids.length>0 ){
            //定义一个集合存所有符合itemcat的id
            List <Long>list=new ArrayList();

            //如果是审核通过
            //本级通过，上级也得审核通过，本级的parentId是上级的Id;直到上级的parentId=0才没有上级
            if("1".equals(status)){
                for (long id : ids) {
                    list.add(id);
                    //通过id获得到itemcat
                    ItemCat itemCat = itemCatDao.selectByPrimaryKey(id);
                    //判断itemcat的parentId
                    //  如果是一级分类
                    if(itemCat.getParentId()==0){
                        //添加自己的id
                        list.add(id);
                    }
                    //如果是2,3级
                    if(itemCat.getParentId()!=0){
                        //添加自己的id
                        list.add(id);

                        //获取上级的iD
                        long id1=itemCat.getParentId();

                        //获得上级itemcat
                        ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(id1);

                        //2级分类
                        if(itemCat1.getParentId()==0){
                            // //添加1级的id
                            list.add(id1);
                        }

                        //3级分类
                        if(itemCat1.getParentId()!=0){
                            //添加2级的id
                            list.add(id1);
                            //添加3级的id
                            list.add(itemCat1.getParentId());
                        }
                    }
                }
            }

            //如果是驳回，上级不通过，下级也不能通过，
            if("2".equals(status)){
                for (long id : ids) {
                    //通过id获得到itemcat
                    ItemCat itemCat = itemCatDao.selectByPrimaryKey(id);

                    //判断itemcat的parentId

                    //  如果是一级分类，一级分类下的所有的都不通过
                    if(itemCat.getParentId()==0){
                        //添加自己的id
                        list.add(id);
                        //获得的1级parentId
                        long parentId1=itemCat.getParentId();
                        ItemCatQuery itemCatQuery1=new ItemCatQuery();
                        itemCatQuery1.createCriteria().andParentIdEqualTo(parentId1);
                        //获取到所有的2级的itemcat1
                        List<ItemCat> itemCats = itemCatDao.selectByExample(itemCatQuery1);
                        for (ItemCat itemCat1 : itemCats) {
                            //添加2级id
                            list.add(itemCat1.getId());
                            //获得2级的parentId2
                            long parentId2 = itemCat1.getParentId();
                            ItemCatQuery itemCatQuery2=new ItemCatQuery();
                            itemCatQuery2.createCriteria().andParentIdEqualTo(parentId2);
                            List<ItemCat> itemCatListS = itemCatDao.selectByExample(itemCatQuery2);
                            //获取到所有的3级的itemcat2
                            for (ItemCat itemCat2 : itemCatListS) {
                                //添加3级id
                                list.add(itemCat2.getId());
                            }
                        }
                    }

                    //如果是2,3级
                    if(itemCat.getParentId()!=0){
                        //获取上级的iD
                        long id1=itemCat.getParentId();
                        //获得上级itemcat
                        ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(id1);

                        //2级分类
                        if(itemCat1.getParentId()==0){
                            //添加自己的id
                            list.add(id);

                           //查出2级分类的parentId
                            long parentId1=itemCat.getParentId();
                            ItemCatQuery itemCatQuery3=new ItemCatQuery();
                            itemCatQuery3.createCriteria().andParentIdEqualTo(parentId1);
                            //获取所有的三级itemcat
                            List<ItemCat> itemCats = itemCatDao.selectByExample(itemCatQuery3);
                            for (ItemCat itemcat3 : itemCats) {
                                //添加三级id
                                list.add(itemcat3.getId());
                            }
                        }

                        //3级分类
                        if(itemCat1.getParentId()!=0){
                            //添加自己的id
                            list.add(id);
                        }
                    }
                }

            }


            System.out.println("此时集合的长度"+list.size());
            //最后更新所有itemcat的id为集合中的id
           ItemCat newItemCat =new ItemCat();
            newItemCat.setStatus(status);
            //遍历集合
            for (Long id : list) {
                newItemCat.setId(id);
                itemCatDao.updateByPrimaryKeySelective(newItemCat);
            }


        }

    }


}
