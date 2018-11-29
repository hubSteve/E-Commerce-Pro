package cn.itcast.core.service.brand;


import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Resource
    private BrandDao brandDao;

    @Override
    public List<Brand> findAllBrands() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageInfo<Brand> findBrandsByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Brand> brandList = brandDao.selectByExample(null);
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);
        return pageInfo;
    }

    @Override
    public PageInfo<Brand> findBrandsBySearch(Integer pageNum, Integer pageSize, Brand brand) {
        PageHelper.startPage(pageNum,pageSize);
        BrandQuery brandQuery=new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (brand.getName()!=null && !"".equals(brand.getName().trim())){
            criteria.andNameLike("%"+brand.getName().trim()+"%");
        }
        if (brand.getFirstChar()!=null && !"".equals(brand.getFirstChar().trim())){
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }
        brandQuery.setOrderByClause("id desc");
        List<Brand> brandList = brandDao.selectByExample(brandQuery);
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);
        return pageInfo;
    }

    @Transactional
    @Override
    public void addBrand(Brand brand) {
        brandDao.insertSelective(brand);
    }

    @Override
    public Brand findOne(long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Transactional
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    @Transactional
    @Override
    public void delete(long[] ids) {
        if (ids!=null && ids.length>0){
            brandDao.deleteByPrimaryKeys(ids);
        }
    }

    @Override
    public List<Map<String, String>> selectOptionList() {
        return  brandDao.selectOptionList();
    }

    //显示所有状态为1的品牌
    @Override
    public PageResult searchforStatus(Integer pageNum, Integer pageSize, Brand brand) {
        PageHelper.startPage(pageNum,pageSize);
        BrandQuery brandQuery=null;
        if(brand!=null){
            brandQuery=new BrandQuery();
            BrandQuery.Criteria criteria = brandQuery.createCriteria();
            if (brand.getName()!=null && !"".equals(brand.getName().trim())){
                criteria.andNameLike("%"+brand.getName().trim()+"%");
            }
            if (brand.getFirstChar()!=null && !"".equals(brand.getFirstChar().trim())){
                criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
            }
            if(brand.getStatus()!=null && !"".equals(brand.getStatus().trim())){
                criteria.andStatusEqualTo(brand.getStatus().trim());
            }
            brandQuery.setOrderByClause("id desc");
        }

        Page<Brand> page= (Page<Brand>) brandDao.selectByExample(brandQuery);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //审核品牌
    @Override
    @Transactional
    public void updateStatus(long[] ids, String status) {
        if(ids!=null && ids.length>0){
            Brand brand=new Brand();
            brand.setStatus(status);
            for (long id : ids) {
                brand.setId(id);
                brandDao.updateByPrimaryKeySelective(brand);
            }
        }

    }
}
