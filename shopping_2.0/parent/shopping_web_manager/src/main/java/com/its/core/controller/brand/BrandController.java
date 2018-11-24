package com.its.core.controller.brand;


import com.alibaba.dubbo.config.annotation.Reference;
import com.its.core.entity.PageResult;
import com.its.core.entity.Result;
import com.its.core.pojo.good.Brand;
import com.its.core.service.brand.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    /**注入*/
    @Reference
    private BrandService brandService;

    /**
     * 查询全部商品信息
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<Brand> findAll(){
        return brandService.findAll();
    }


    /**
     * 分页显示数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(Integer pageNum, Integer pageSize){
        return  brandService.findPage(pageNum,pageSize);
        }

    /**
     * 根据条件查新
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer pageNum, Integer pageSize,@RequestBody Brand brand){
        //添加查询参数
         PageResult search = brandService.search(pageNum, pageSize, brand);
         return search;
     }

    /**
     * 添加品牌信息
     * @param brand
     * @return
     */
     @RequestMapping("/add.do")
    public Result add ( @RequestBody Brand brand){
        try {
            brandService.add(brand);
            return  new Result(true ,"保存成功");
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,"7保存失败");
        }
     }

    /**
     * 根据id 查寻 进行数据回显
     * @param id
     * @return
     */
     @RequestMapping("/findOne.do")
    public Brand findOne(Long id){
         Brand one = brandService.findOne(id);
         return one;
     }

    /**
     * 更新品牌信息
     * @param brand
     * @return
     */
     @RequestMapping("/update.do")
    public Result update (@RequestBody Brand brand) {
         try {
             brandService.update(brand);
             return new  Result(true,"更新成功");
         }catch (Exception e){
             e.printStackTrace();
             return new  Result(false,"更新失败");
         }
     }

    /**
     * 删除品牌信息
     * @param ids
     * @return
     */
     @RequestMapping("/delete.do")
    public Result delete(Long[] ids){
         try {
             brandService.delete(ids);
             return new  Result(true,"删除成功");
         } catch (Exception e) {
             e.printStackTrace();
             return new  Result(false,"删除失败");
         }
     }
}
