package cn.itcast.core.controller.orders;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.orders.OrdersQueryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersQueryController {

    @Reference
    private OrdersQueryService ordersQueryService;
    @RequestMapping("/OrdersQuery.do")
    public PageResult OrdersQuery(Integer pageNum, Integer pageSize,@RequestBody Order order){

        return ordersQueryService.OrdersQuery(pageNum,pageSize,order);
    }
}
