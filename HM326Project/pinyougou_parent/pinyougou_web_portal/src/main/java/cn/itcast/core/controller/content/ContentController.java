package cn.itcast.core.controller.content;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.content.ContentService;
import cn.itcast.core.service.itemCat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("findByCategoryId")
    public List<Content> findByCategoryId(long categoryId){
        System.out.println("广告2");
        return contentService.findByCategoryId(categoryId);
    }


    @RequestMapping("findItemCatList")
    public List<ItemCat> findItemCatList(){
        List<ItemCat> itemCatList = itemCatService.findItemCatList();
        System.out.println(itemCatList);
        return itemCatList;
    }



}
