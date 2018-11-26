package cn.itcast.core.controller.itemCat;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemCat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("findByParentId")
    public List<ItemCat> findByParentId(long parentId){
        return itemCatService.findByParentId(parentId);
    }

    @RequestMapping("add")
    public Result add(@RequestBody ItemCat itemCat){
        try {
            itemCatService.add(itemCat);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("findAll")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }

}
