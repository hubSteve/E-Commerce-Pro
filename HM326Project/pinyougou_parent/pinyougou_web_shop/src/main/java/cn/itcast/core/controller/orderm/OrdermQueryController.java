package cn.itcast.core.controller.orderm;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.orderm.OrdermQueryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orderm")
public class OrdermQueryController {
    @Reference
    private OrdermQueryService ordermQueryService;

    @RequestMapping("/OrdermQuery.do")

    public PageResult OrdermQuery(Integer pageNum, Integer pageSize, @RequestBody Order order) {
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(sellerId);
        return ordermQueryService.OrdermQuery(pageNum, pageSize, order);
    }

    @RequestMapping("/orderf.do")
    public PageResult orderf(Integer pageNum, Integer pageSize, @RequestBody Order order) {
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(sellerId);
        return ordermQueryService.OrdermQuery(pageNum, pageSize, order);
    }
}