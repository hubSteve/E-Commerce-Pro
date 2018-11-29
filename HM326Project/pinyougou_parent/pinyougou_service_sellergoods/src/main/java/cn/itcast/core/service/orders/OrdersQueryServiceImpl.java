package cn.itcast.core.service.orders;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class OrdersQueryServiceImpl implements OrdersQueryService {
    @Autowired
    private OrderDao orderDao;
    @Override
    public PageResult OrdersQuery(Integer pageNum, Integer pageSize, Order order) {
        // 设置分页条件
        PageHelper.startPage(pageNum, pageSize);
        // 设置查询条件
        OrderQuery query = new OrderQuery();
        if (order.getUserId()!=null&&!"".equals(order.getUserId().trim())){
           query.createCriteria().andUserIdLike("%"+order.getUserId().trim()+"%");
        }
        if (order.getSellerId()!=null&&!"".equals(order.getSellerId().trim())){
            query.createCriteria().andSellerIdLike("%"+order.getSellerId().trim()+"%");
        }
        PageHelper.orderBy("create_time desc");
        Page<Order> page = (Page<Order>) orderDao.selectByExample(query);
        return new PageResult(page.getTotal(),page.getResult());
    }
}
