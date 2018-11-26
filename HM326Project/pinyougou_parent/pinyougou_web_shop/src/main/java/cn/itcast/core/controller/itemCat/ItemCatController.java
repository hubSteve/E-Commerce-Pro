package cn.itcast.core.controller.itemCat;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemCat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
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

    @RequestMapping("findOne")
    public ItemCat findOne(long id){
        return itemCatService.findOne(id);
    }

    @RequestMapping("findAll")
    public List<ItemCat> findAll(){
        return itemCatService.findAll();
    }

}
