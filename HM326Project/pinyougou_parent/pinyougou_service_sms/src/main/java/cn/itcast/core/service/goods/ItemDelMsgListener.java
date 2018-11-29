package cn.itcast.core.service.goods;


import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.fastjson.JSON;
import org.omg.CORBA.ULongLongSeqHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者 : 黄执林   业务MQ商品下架和静态页面删除
 */
public class ItemDelMsgListener implements MessageListener {
    @Autowired
    private ItemDao itemDao;

    @Value("${pagedir}")
    private String pageDir;


    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 删除索引库中的相关数据
     * @param message
     */
    @Override
    @Transactional
    public void onMessage(Message message) {
        MapMessage msg = (MapMessage)message;
        try {
            String idsStr = msg.getString("goodsIds");
            String[] itemIdArray = idsStr.split("-");
            ItemQuery itemQuery = new ItemQuery();
            ItemQuery.Criteria criteria = itemQuery.createCriteria();
            //创建一个方法删除静态页面
            if (itemIdArray.length > 0 && itemIdArray != null) {
                for (String s : itemIdArray) {
                    criteria.andGoodsIdEqualTo(Long.valueOf(s));
                    List<Item> items = itemDao.selectByExample(itemQuery);
                    if (items.size() > 0 && items != null) {
                        for (Item item : items) {
                            solrTemplate.deleteById(item.getId()+"");
                            solrTemplate.commit();
                        }
                    }
                }
                delFMarkerFile(itemIdArray);
                System.out.println("<<<<<=================================>>>>>文件删除了");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除静态页面
     * @param itemIdArray
     */
    private void delFMarkerFile(String[] itemIdArray) {
        File file = new File(pageDir);
        File[] fileArray = searchFiles(file); //调用方法查询
        if (itemIdArray.length > 0 && itemIdArray != null) {
            for (String id: itemIdArray) {
                for (int e = 0; e < fileArray.length - 1; e++) {
                    String name = fileArray[e].getName();
                    String prefix = name.substring(0, name.indexOf("."));
                    if (id.equals(prefix)) {
                        fileArray[e].delete();
                    }
                }
            }
        }
    }

    private File[] searchFiles(File file) {

        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile()) {
                    return true;
                }else{
                    return false;
                }
            }
        });
        return files;
    }


}
