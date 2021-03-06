package cn.itcast.core.service.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface BrandService {

    List<Brand> findAllBrands();

    PageInfo<Brand> findBrandsByPage(Integer pageNum,Integer pageSize);

    PageInfo<Brand> findBrandsBySearch(Integer pageNum,Integer pageSize,Brand brand);

    void addBrand(Brand brand);

    Brand findOne(long id);

    void update(Brand brand);

    void delete(long[] ids);

    List<Map<String,String>> selectOptionList();
    //显示所有状态为1的品牌
    PageResult searchforStatus(Integer pageNum, Integer pageSize, Brand brand);
    //审核品牌
    void updateStatus(long[]ids,String status);

	void addBrandList(List<Brand> brandList);
}
