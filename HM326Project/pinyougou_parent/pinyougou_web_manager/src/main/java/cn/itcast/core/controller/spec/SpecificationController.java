package cn.itcast.core.controller.spec;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.service.spec.SpecificationService;
import cn.itcast.core.vo.SpecificationVo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("specification")
public class SpecificationController {

    @Reference
    private SpecificationService specificationService;

    @RequestMapping("search")
    public PageInfo<Specification> search(Integer page, Integer rows, @RequestBody Specification specification){
       return specificationService.search(page,rows,specification);
    }

    @RequestMapping("add")
    public Result add(@RequestBody SpecificationVo specificationVo){
        try {
            specificationService.add(specificationVo);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    @RequestMapping("findOne")
    public SpecificationVo findOne(long id){
       return specificationService.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody SpecificationVo specificationVo){
        try {
            specificationService.update(specificationVo);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    @RequestMapping("delete")
    public Result delete(long[] ids){
        try {
            specificationService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("selectOptionList")
    public List<Map<String,String>> selectOptionList(){
        return specificationService.selectOptionList();
    }

    //显示所有status=0的规格
    @RequestMapping("findSpecByStatus")
    public PageResult findSpecByStatus(Integer page, Integer rows, @RequestBody Specification specification){
        PageResult pageResult = specificationService.findSpecByStatus(page, rows, specification);
        return pageResult;
    }
    //审核方法
    @RequestMapping("updateStatus")
    public Result updateStatus(long[]ids,String status){
       try{
        specificationService.updateStatus(ids, status);
        return new Result(true,"操作成功");
       }catch (Exception e){
           e.printStackTrace();
           return new Result(false,"操作失败");
       }
    }
}
