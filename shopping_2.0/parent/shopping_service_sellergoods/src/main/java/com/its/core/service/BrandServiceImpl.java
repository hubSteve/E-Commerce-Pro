package com.its.core.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.its.core.dao.good.BrandDao;
import com.its.core.entity.PageResult;
import com.its.core.pojo.good.Brand;
import com.its.core.pojo.good.BrandQuery;
import com.its.core.service.brand.BrandService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {
    /**注入*/
    @Resource
    private BrandDao brandDao;

    /**
     * 查询全不品牌信息
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        /*设置分页参数*/
          PageHelper.startPage(pageNum,pageSize);
        /*封装到*/
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;

    }

    /**
     * 条件查询
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */
    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Brand brand) {
        //设置分页条件
        PageHelper.startPage(pageNum,pageSize);
        //设置查询条件
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        //传递数据进行判断 去除空格
        if (brand.getName() != null && !"".equals(brand.getName().trim())){
            //添加模糊查询条件  根据 品牌名称查询
            criteria.andNameLike("%"+brand.getName().trim()+"%");
        }
        //根据商品首字母进行查询
        if (brand.getFirstChar()!=null && !"".equals(brand.getFirstChar().trim())){
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }
        //优先显示 进行降序
        brandQuery.setOrderByClause("id desc");
        //获取查询 条件返回 结果集
        Page<Brand> page= (Page<Brand>) brandDao.selectByExample(brandQuery);
        //返回结果
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加品牌
     * @param brand
     */
    @Transactional
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    /**
     * 根据id 查寻 进行数据回显
     * @param id
     * @return
     */
    @Override
    public Brand findOne(Long id) {
        Brand brand = brandDao.selectByPrimaryKey(id);
        return brand;
    }

    /**
     * 跟新品牌名称
     * @param brand
     */
    @Transactional
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    /**
     * 删除品牌信息
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids != null && ids.length > 0){
            brandDao.deleteByPrimaryKeys(ids);

        }
    }
}
