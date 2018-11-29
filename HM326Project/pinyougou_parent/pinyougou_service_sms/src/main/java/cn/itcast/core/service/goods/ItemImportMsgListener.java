package cn.itcast.core.service.goods;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;
import java.util.Map;
/**
 * 作者 : 黄执林   业务MQ商品导入索引库和生成静态页面
 */
public class ItemImportMsgListener implements MessageListener {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 向索引库导入审核通过的商品数据
     * @param message
     */
    @Override
    @Transactional
    public void onMessage(Message message) {
        MapMessage msg = (MapMessage)message;

        try {
            String goodsIdForImport = msg.getString("goodsIdForImport");
            String[] ids = goodsIdForImport.split("-");
            System.out.println("======================================>>>>>" + ids.toString());
            ItemQuery itemQuery = new ItemQuery();
            ItemQuery.Criteria criteria = itemQuery.createCriteria();
                if (ids != null && ids.length > 0) {
                    for (String goodsId : ids) {
                        criteria.andGoodsIdEqualTo(Long.parseLong(goodsId));
                        List<Item> itemList = itemDao.selectByExample(itemQuery);

                            for (Item item : itemList) {
                                String spec = item.getSpec();
                                Map specMap = JSON.parseObject(spec, Map.class);
                                item.setSpecMap(specMap);
                            }
                            solrTemplate.saveBeans(itemList);
                            solrTemplate.commit();
                    }
                }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
