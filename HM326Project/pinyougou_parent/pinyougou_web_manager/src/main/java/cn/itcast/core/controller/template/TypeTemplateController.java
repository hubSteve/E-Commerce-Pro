package cn.itcast.core.controller.template;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.service.template.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService typeTemplateService;

    @RequestMapping("search")
    public PageInfo<TypeTemplate> search(Integer page, Integer rows, @RequestBody TypeTemplate typeTemplate){
        PageInfo<TypeTemplate> pageInfo = typeTemplateService.search(page, rows, typeTemplate);
        return pageInfo;
    }

    @RequestMapping("add")
    public Result add(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("findOne")
    public TypeTemplate findOne(long id){
        return typeTemplateService.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody TypeTemplate typeTemplate){
        try {
            typeTemplateService.update(typeTemplate);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    //查询status==1的所有的模板
    @RequestMapping("searchTemlListByStatus")
    public PageResult searchTemlListByStatus(Integer page, Integer rows, @RequestBody TypeTemplate typeTemplate){
        PageResult pageResult = typeTemplateService.searchTemlListByStatus(page, rows, typeTemplate);
        return pageResult;
    }

    //审核的方法
    @RequestMapping("updateStatus")
    public Result updateStatus(long[]ids,String status){
        try{
            typeTemplateService.updateStatus(ids, status);
            return new Result(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }
}
