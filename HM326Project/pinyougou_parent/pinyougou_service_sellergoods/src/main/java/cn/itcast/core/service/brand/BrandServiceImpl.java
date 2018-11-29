package cn.itcast.core.service.brand;


import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
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
        if (brand.getStatus()!=null && !"".equals(brand.getStatus().trim())){
            criteria.andStatusLike("%"+brand.getStatus().trim()+"%");
        }

        brandQuery.setOrderByClause("id desc");
        List<Brand> brandList = brandDao.selectByExample(brandQuery);
        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);
        return pageInfo;
    }

    @Transactional
    @Override
    public void addBrand(Brand brand) {
        brand.setStatus("0");
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
	
	@Override
    public void addBrandList(List<Brand> brandList) {
        List<Brand> brands = brandDao.selectByExample(null);
        List<String> brandNameList=new ArrayList<>();
        if (brands!=null && brands.size()>0){
            for (Brand brand : brands) {
                brandNameList.add(brand.getName());
            }
        }

        if (brandList!=null && brandList.size()>0){
            for (Brand newbrand : brandList) {
                if (brandNameList.contains(newbrand.getName())){
                    continue;
                }
                brandDao.insertSelective(newbrand);
            }
        }
    }
}
