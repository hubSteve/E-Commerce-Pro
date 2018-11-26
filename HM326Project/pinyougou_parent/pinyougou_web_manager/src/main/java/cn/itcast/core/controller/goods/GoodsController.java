package cn.itcast.core.controller.goods;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.goods.GoodsService;
import cn.itcast.core.service.page.ItemPageService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goods")
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemPageService itemPageService;

    @RequestMapping("search")
    public PageInfo<Goods> search(Integer page,Integer rows,@RequestBody Goods goods){
        return goodsService.searchForManager(page,rows,goods);
    }

    @RequestMapping("updateStatus")
    public Result updateStatus(long[] ids,String status){
        try {
            goodsService.updateStatus(ids,status);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }

    @RequestMapping("delete")
    public Result delete(long[] ids){
        try {
            goodsService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

    @RequestMapping("getHtml")
    public void getHtml(long goodsId){
        itemPageService.getItemHtml(goodsId);
    }

}
