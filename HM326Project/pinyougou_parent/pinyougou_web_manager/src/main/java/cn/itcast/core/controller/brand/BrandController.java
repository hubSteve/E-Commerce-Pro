package cn.itcast.core.controller.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.service.brand.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("findAll")
    public List<Brand> findAllBrands(){
       return brandService.findAllBrands();
    }

    @RequestMapping("findPage")
    public PageInfo<Brand> findBrandsByPage(Integer pageNum,Integer pageSize){
        PageInfo<Brand> pageInfo = brandService.findBrandsByPage(pageNum, pageSize);
        return pageInfo;
    }

    @RequestMapping("search")
    public PageInfo<Brand> findBrandsBySearch(Integer pageNum, Integer pageSize, @RequestBody Brand brand){
        PageInfo<Brand> pageInfo = brandService.findBrandsBySearch(pageNum, pageSize,brand);
        return pageInfo;
    }

    @RequestMapping("add")
    public Result addBrand(@RequestBody Brand brand){
        try {
            brandService.addBrand(brand);
            return new Result(true,"保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"保存失败");
        }

    }

    @RequestMapping("findOne")
    public Brand findOne(long id){
        return brandService.findOne(id);
    }

    @RequestMapping("update")

    public Result update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return new Result(true, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "更新失败");
        }
    }

    @RequestMapping("delete")
    public Result delete(long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("selectOptionList")
    public List<Map<String,String>> selectOptionList(){
        return brandService.selectOptionList();
    }

    //显示所有状态为1的品牌
    @RequestMapping("searchforStatus")
    public PageResult searchforStatus(Integer pageNum, Integer pageSize, @RequestBody Brand brand){
        PageResult pageResult = brandService.searchforStatus(pageNum, pageSize, brand);
        return pageResult;
    }
    //审核品牌
    @RequestMapping("updateStatus")
    public Result updateStatus(long[]ids,String status){
        try{
            brandService.updateStatus(ids,status);
            return new Result(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }
}
