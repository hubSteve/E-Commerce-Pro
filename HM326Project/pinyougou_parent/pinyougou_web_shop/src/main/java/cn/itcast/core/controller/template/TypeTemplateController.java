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


    @RequestMapping("search")
    public PageInfo<TypeTemplate> search(Integer page, Integer rows, @RequestBody TypeTemplate typeTemplate){

        System.out.println("第三个发动机可发挥过圣诞节和分公司电话"+typeTemplateService.search(page,rows,typeTemplate));
        return typeTemplateService.search(page,rows,typeTemplate);
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


    /** 批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(long[] ids){

        try {
            typeTemplateService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }

    }
}
