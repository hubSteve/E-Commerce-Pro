package cn.itcast.core.controller.itemCat;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.itemCat.ItemCatService;
import cn.itcast.core.service.template.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;


    @Reference
    private TypeTemplateService typeTemplateService;

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


    /** 添加商品分类 查询模板信息
     * @return
     */
    @RequestMapping("/selectTypeTemplateList.do")
    public List<Map> selectTypeTemplateList(){

        return typeTemplateService.selectTypeTemplateList();

    }


    //修改回显数据
    @RequestMapping("/findOne.do")
    public ItemCat findOne(Long id) throws Exception {
        return itemCatService.findOne(id);
    }


    //修改数据
    @RequestMapping("/update.do")
    public Result update(@RequestBody ItemCat itemCat){
        try {
            System.out.println("发放好久考试"+itemCat);
            itemCatService.update(itemCat);
            return new Result(true ,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false ,"修改失败");
        }
    }


    /** 批量删除商品分类
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(long[] ids ){

        try {
            itemCatService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }


    @RequestMapping("findOne")
    public ItemCat findOne(long id){
        return itemCatService.findOne(id);
    }

    //查询所有的statsu==0的商品分类
    @RequestMapping("searchItemCatListByStatus")
    public PageResult searchItemCatListByStatus(Integer page,Integer rows,@RequestBody ItemCat itemCat){
        PageResult pageResult = itemCatService.searchItemCatListByStatus(page, rows, itemCat);
        return pageResult;
    }

    //审核
    @RequestMapping("updateStatus")
    public Result updateStatus(long[]ids,String status){
        try{
            itemCatService.updateStatus(ids, status);
            return new Result(true,"操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"操作失败");

        }
    }
}
