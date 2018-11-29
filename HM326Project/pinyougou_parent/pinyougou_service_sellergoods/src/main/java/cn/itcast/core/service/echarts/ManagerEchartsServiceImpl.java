package cn.itcast.core.service.echarts;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.order.Order;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ManagerEchartsServiceImpl implements ManagerEchartsService {

    @Resource
    private OrderDao orderDao;

    @Override
    public LinkedHashMap<String, Double> echartsData() {
        //一天的毫秒数值
        long oneDay=1000*24*60*60;
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //起始时间
            Date startDate = df.parse("2017-08-24 00:00:00");
            long startTime = startDate.getTime();
            List<Order> allOrderList = orderDao.selectByExample(null);
            List<Order> list1=new ArrayList<>();
            List<Order> list2=new ArrayList<>();
            List<Order> list3=new ArrayList<>();
            List<Order> list4=new ArrayList<>();
            List<Order> list5=new ArrayList<>();
            for (Order order : allOrderList) {
                Date orderCreateTime = order.getCreateTime();
                long time = orderCreateTime.getTime();
                if (time >= startTime && time <=(startTime+oneDay)){
                    list1.add(order);
                }else if (time >= (startTime+oneDay) && time <=(startTime+2*oneDay)){
                    list2.add(order);
                }else if (time >= (startTime+2*oneDay) && time <=(startTime+3*oneDay)){
                    list3.add(order);
                }else if (time >= (startTime+3*oneDay) && time <=(startTime+4*oneDay)){
                    list4.add(order);
                }else if (time >= (startTime+4*oneDay) && time <=(startTime+5*oneDay)){
                    list5.add(order);
                }
            }

            Double count1 = (double)Math.round(getCount(list1)*100)/100;
            Double count2 = (double)Math.round(getCount(list2)*100)/100;
            Double count3 = (double)Math.round(getCount(list3)*100)/100;
            Double count4 = (double)Math.round(getCount(list4)*100)/100;
            Double count5 = (double)Math.round(getCount(list5)*100)/100;

            LinkedHashMap<String,Double> list=new LinkedHashMap<>();
            list.put("2017年8月24日",count1);
            list.put("2017年8月25日",count2);
            list.put("2017年8月26日",count3);
            list.put("2017年8月27日",count4);
            list.put("2017年8月28日",count5);
            return list;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Double getCount(List<Order> list){
        double count=0;
        if (list.size()>0){
            for (Order order : list) {
                count+=(order.getPayment().doubleValue());
            }
        }
        return count;
    }
}
