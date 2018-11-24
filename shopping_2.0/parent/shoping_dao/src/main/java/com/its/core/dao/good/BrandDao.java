package com.its.core.dao.good;

import com.its.core.pojo.good.Brand;
import com.its.core.pojo.good.BrandQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BrandDao {
    int countByExample(BrandQuery example);

    int deleteByExample(BrandQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Brand record);

    int insertSelective(Brand record);

    List<Brand> selectByExample(BrandQuery example);

    Brand selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Brand record, @Param("example") BrandQuery example);

    int updateByExample(@Param("record") Brand record, @Param("example") BrandQuery example);

    int updateByPrimaryKeySelective(Brand record);

    int updateByPrimaryKey(Brand record);

    /*删除品牌*/
    void deleteByPrimaryKeys(Long[] ids);
}