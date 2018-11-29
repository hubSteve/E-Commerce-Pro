package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.goods.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {
    @Reference
    private SeckillGoodsService seckillGoodsService;

    @RequestMapping("/add.do")
    public Result add(@RequestBody Goods goods){
        try {
            seckillGoodsService.add(goods);
            return new Result(true, "申请添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "申请添加失败");
        }
    }

}
