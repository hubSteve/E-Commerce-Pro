package cn.itcast.core.controller.content;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.service.content.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @RequestMapping("findByCategoryId")
    public List<Content> findByCategoryId(long categoryId){
        System.out.println("广告2");
        return contentService.findByCategoryId(categoryId);
    }
}
