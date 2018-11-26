package cn.itcast.core.controller.seckill;


import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.seckill.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("seckillGoods")
public class SeckillController {

    @Reference(timeout = 10000)
    private SeckillGoodsService seckillGoodsService;


    @RequestMapping("findList")
    public List<SeckillGoods> findList(){
        return seckillGoodsService.findList();
    }

    @RequestMapping("findOneFromRedis")
    public SeckillGoods findOneFromRedis(Long id){
        return seckillGoodsService.findOneFromRedis(id);
    }
}
