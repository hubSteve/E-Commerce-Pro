package cn.itcast.core.goods;


import cn.itcast.core.dao.good.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;


import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;



public class GoodsMessageListener implements MessageListener {
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public void onMessage(Message message) {
        MapMessage msg = (MapMessage)message;
        try {
            String goodsIds = msg.getString("goodsIds");
            System.out.println(goodsIds);

        /*    Goods goods = new Goods();
            goods.setId(Long.valueOf(goodsId));
            goods.setIsMarketable("0");
            goodsDao.updateByPrimaryKeySelective(goods);

            SimpleQuery simpleQuery = new SimpleQuery();
            Criteria criteria = new Criteria();
            criteria.or("item_goodsid").is();*/
        } catch (JMSException e) {
            e.printStackTrace();
        }


    }
}
