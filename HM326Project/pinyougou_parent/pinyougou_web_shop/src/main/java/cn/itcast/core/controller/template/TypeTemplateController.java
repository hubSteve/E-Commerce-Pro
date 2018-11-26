package cn.itcast.core.controller.template;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.template.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;


    @RequestMapping("findOne")
    public TypeTemplate findOne(long id){
        return typeTemplateService.findOne(id);
    }

    @RequestMapping("findBySpecList")
    public List<Map> findBySpecList(long id){
        return typeTemplateService.findBySpecList(id);
    }

}
