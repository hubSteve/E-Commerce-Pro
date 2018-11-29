package cn.itcast.core.service.spec;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.vo.SpecificationVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


public interface SpecificationService {

    PageInfo<Specification> search(Integer page,Integer rows,Specification specification);

    void add(SpecificationVo specificationVo);

    SpecificationVo findOne(long id);

    void update(SpecificationVo specificationVo);

    void delete(long[] ids);

    List<Map<String,String>> selectOptionList();

    //显示status==1的规格
    PageResult findSpecByStatus(Integer page, Integer rows, Specification specification);

    //审核
    void updateStatus(long[]ids,String status);
}
