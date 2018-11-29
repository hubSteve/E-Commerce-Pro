package cn.itcast.core.service.goods;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.service.page.ItemPageService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GoodsDescDao goodsDescDao;
    @Resource
    private ItemDao itemDao;

    @Resource
    private SellerDao sellerDao;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private BrandDao brandDao;

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination delMSGDestination;

    @Resource
    private Destination importMSGDestination;

    @Reference
    private ItemPageService itemPageService;


    private HashMap map = new HashMap();



    @Transactional
    @Override

    public void add(GoodsVo goodsVo) {
        Goods goods = goodsVo.getGoods();
        goods.setAuditStatus("0");
        //黄执林添加  将是否删除字段添加为0,这样方便以后操作
        goods.setIsDelete("0");//0为未删除,1为删除
        goodsDao.insertSelective(goods);

        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getId());
        goodsDescDao.insertSelective(goodsDesc);

        //添加库存
        if ("1".equals(goods.getIsEnableSpec())) { //启用规格
            List<Item> itemList = goodsVo.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (Item item : itemList) {
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    //{"机身内存":"16G","网络":"联通3G"}
                    Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
                    Set<Map.Entry<String, String>> entrySet = map.entrySet();
                    for (Map.Entry<String, String> entry : entrySet) {
                        title += " " + entry.getValue();
                    }
                    item.setTitle(title);
                    setAttributeToItem(goods, goodsDesc, item);
                    itemDao.insertSelective(item);

                }
            }
        } else {

            Item item = new Item();
            String title = goods.getGoodsName() + " " + goods.getCaption();
            item.setTitle(title);
            item.setPrice(goods.getPrice());
            item.setNum(9999);
            item.setIsDefault("1");
            item.setSpec("{}");
            setAttributeToItem(goods, goodsDesc, item);
            itemDao.insertSelective(item);

        }

    }

    @Override
    public PageInfo<Goods> search(Integer page, Integer rows, Goods goods) {
        PageHelper.startPage(page, rows);
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if (goods.getSellerId() != null && !"".equals(goods.getSellerId().trim())) {
            criteria.andSellerIdEqualTo(goods.getSellerId().trim());
        }

        if (goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus().trim())) {
            criteria.andAuditStatusEqualTo(goods.getAuditStatus().trim());
        }

        if (goods.getGoodsName() != null && !"".equals(goods.getGoodsName().trim())) {
            criteria.andGoodsNameLike("%" + goods.getGoodsName().trim() + "%");
        }

        goodsQuery.setOrderByClause("id desc");

        List<Goods> goodsList = goodsDao.selectByExample(goodsQuery);
        PageInfo<Goods> pageInfo = new PageInfo<>(goodsList);
        return pageInfo;
    }


    private void setAttributeToItem(Goods goods, GoodsDesc goodsDesc, Item item) {

        //商品图片
        List<Map> list = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
        if (list != null && list.size() > 0) {
            String url = list.get(0).get("url").toString();
            item.setImage(url);
        }

        item.setCategoryid(goods.getCategory3Id());
        item.setStatus("1");
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setGoodsId(goods.getId());
        String sellerId = goods.getSellerId();
        item.setSellerId(sellerId);
        // 商家店铺名称
        item.setSeller(sellerDao.selectByPrimaryKey(sellerId).getNickName());
        // 三级分类名称
        item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
        // 品牌名称
        item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());
    }


    @Override
    public GoodsVo findOne(Long id) {
        GoodsVo goodsVo = new GoodsVo();
        Goods goods = goodsDao.selectByPrimaryKey(id);
        goodsVo.setGoods(goods);
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        goodsVo.setGoodsDesc(goodsDesc);
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goods.getId());
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        goodsVo.setItemList(itemList);
        return goodsVo;
    }


    @Transactional
    @Override
    public void update(GoodsVo goodsVo) {
        Goods goods = goodsVo.getGoods();
        goods.setAuditStatus("0");
        goodsDao.updateByPrimaryKeySelective(goods);
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDescDao.updateByPrimaryKeySelective(goodsDesc);

        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goods.getId());
        itemDao.deleteByExample(itemQuery);

        //添加库存
        if ("1".equals(goods.getIsEnableSpec())) { //启用规格
            List<Item> itemList = goodsVo.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (Item item : itemList) {
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    //{"机身内存":"16G","网络":"联通3G"}
                    Map<String, String> map = JSON.parseObject(item.getSpec(), Map.class);
                    Set<Map.Entry<String, String>> entrySet = map.entrySet();
                    for (Map.Entry<String, String> entry : entrySet) {
                        title += " " + entry.getValue();
                    }
                    item.setTitle(title);
                    setAttributeToItem(goods, goodsDesc, item);
                    itemDao.insertSelective(item);
                }
            }
        } else {

            Item item = new Item();
            String title = goods.getGoodsName() + " " + goods.getCaption();
            item.setTitle(title);
            item.setPrice(goods.getPrice());
            item.setNum(9999);
            item.setIsDefault("1");
            item.setSpec("{}");
            setAttributeToItem(goods, goodsDesc, item);
            itemDao.insertSelective(item);

        }

    }

    @Override
    public PageInfo<Goods> searchForManager(Integer page, Integer rows, Goods goods) {
        PageHelper.startPage(page, rows);
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if (goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus().trim())) {
            criteria.andAuditStatusEqualTo(goods.getAuditStatus().trim());
        }
        if (goods.getGoodsName() != null && !"".equals(goods.getGoodsName().trim())) {
            criteria.andGoodsNameLike("%" + goods.getGoodsName().trim() + "%");
        }
        criteria.andIsDeleteIsNull();

        goodsQuery.setOrderByClause("id desc");
        List<Goods> goodsList = goodsDao.selectByExample(goodsQuery);
        PageInfo<Goods> pageInfo = new PageInfo<>(goodsList);
        System.out.println(pageInfo.toString());
        return pageInfo;
    }

    @Transactional
    @Override
    public void updateStatus(long[] ids, String status) {

        String idsStr = "";

        if (ids != null && ids.length > 0) {
            Goods goods = new Goods();
            goods.setIsMarketable(status.equals("1")?"1":"2"); //等于1为上架,2为驳回,则修改状态和索引库
            goods.setAuditStatus(status);
                if ("1".equals(status)) {
                    for (long id : ids) {
                        goods.setId(id);
                        goodsDao.updateByPrimaryKeySelective(goods);
                        //使用MQ消息队列将索引加入索引库
                        idsStr += id + "-";
                        // 生成静态页面
                        itemPageService.getItemHtml(id);
                    }
                    map.put("goodsIdForImport", idsStr);
                    jmsTemplate.convertAndSend(importMSGDestination, map);

                }else{
                        int i = 100;
                        delete(ids, i);
                }
            }
    }


    @Override
    public void delete(long[] ids,int i) {
        String itemIdStr = "";

        if (ids != null && ids.length > 0) {
            Goods goods = new Goods();
            goods.setIsDelete(i==200?"1":""); //1 为删除状态
            for (long id : ids) {
                goods.setId(id);
                goods.setIsMarketable(i==200?"3":"2"); //当i 等于 200 删除, 100是下架
                goodsDao.updateByPrimaryKeySelective(goods);
                itemIdStr += id + "-";
            }
            map.put("goodsIds", itemIdStr);
            jmsTemplate.convertAndSend(delMSGDestination, map);
        }
    }

    @Override
    public List<Goods> findList() {
        return goodsDao.selectByExample(null);
    }
}
