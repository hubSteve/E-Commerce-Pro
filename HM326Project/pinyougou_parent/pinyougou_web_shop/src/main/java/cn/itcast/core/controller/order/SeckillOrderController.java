package cn.itcast.core.controller.order;

import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.order.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    @RequestMapping("/search")
    public PageInfo<SeckillOrder> search(Integer page, Integer rows, @RequestBody SeckillOrder seckillOrder){
        //设置商家id
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        seckillOrder.setSellerId(name);
        return seckillOrderService.search(page,rows,seckillOrder);
    }
}
