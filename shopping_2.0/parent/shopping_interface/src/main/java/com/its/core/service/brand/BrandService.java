package com.its.core.service.brand;

import com.its.core.entity.PageResult;
import com.its.core.pojo.good.Brand;

import java.util.List;

public interface BrandService {

    /**查询全部品牌*/

    public List<Brand> findAll();

    /*查询商品分页显示*/
    public PageResult findPage(Integer pageNum, Integer pageSize);

    /*列表查询*/
    public PageResult search(Integer pageNum, Integer pageSize,Brand brand);
    /*添加商品*/
    public void add (Brand brand);
    /*根据id 查寻 进行数据回显*/
    public Brand findOne(Long id);
    /*更新品牌信息*/
    public void update(Brand brand);
    /*删除品牌名称*/
    public  void  delete(Long[] ids);

}
