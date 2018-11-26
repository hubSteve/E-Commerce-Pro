package cn.itcast.core.service.spec;

import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import cn.itcast.core.vo.SpecificationVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Resource
    private SpecificationDao specificationDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    @Override
    public PageInfo<Specification> search(Integer page, Integer rows, Specification specification) {
        PageHelper.startPage(page,rows);
        SpecificationQuery specificationQuery=new SpecificationQuery();
        if (specification.getSpecName()!=null && !"".equals(specification.getSpecName().trim())){
            specificationQuery.createCriteria().andSpecNameLike("%"+specification.getSpecName().trim()+"%");
        }

        specificationQuery.setOrderByClause("id desc");
        List<Specification> specificationList = specificationDao.selectByExample(specificationQuery);
        PageInfo<Specification> pageInfo=new PageInfo<>(specificationList);

        return pageInfo;
    }

    @Transactional
    @Override
    public void add(SpecificationVo specificationVo) {
        Specification specification = specificationVo.getSpecification();
        specificationDao.insertSelective(specification);

        List<SpecificationOption> specificationOptionList = specificationVo.getSpecificationOptionList();
        if (specificationOptionList!=null && specificationOptionList.size()>0){
            for (SpecificationOption specificationOption : specificationOptionList) {
                specificationOption.setSpecId(specification.getId());
            }
            specificationOptionDao.insertSelectives(specificationOptionList);
        }
    }

    @Override
    public SpecificationVo findOne(long id) {
        SpecificationVo specificationVo=new SpecificationVo();
        Specification specification = specificationDao.selectByPrimaryKey(id);
        specificationVo.setSpecification(specification);

        SpecificationOptionQuery specificationOptionQuery=new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(id);

        List<SpecificationOption> specificationOptionList = specificationOptionDao.selectByExample(specificationOptionQuery);
        specificationVo.setSpecificationOptionList(specificationOptionList);
        return specificationVo;
    }

    @Transactional
    @Override
    public void update(SpecificationVo specificationVo) {
        Specification specification = specificationVo.getSpecification();
        specificationDao.updateByPrimaryKeySelective(specification);

        SpecificationOptionQuery specificationOptionQuery=new SpecificationOptionQuery();
        specificationOptionQuery.createCriteria().andSpecIdEqualTo(specification.getId());

        specificationOptionDao.deleteByExample(specificationOptionQuery);

        List<SpecificationOption> specificationOptionList = specificationVo.getSpecificationOptionList();
        if (specificationOptionList!=null && specificationOptionList.size()>0){
            for (SpecificationOption specificationOption : specificationOptionList) {
                specificationOption.setSpecId(specification.getId());
            }
            specificationOptionDao.insertSelectives(specificationOptionList);
        }


    }

    @Override
    public void delete(long[] ids) {

        if (ids!=null && ids.length>0){
            specificationOptionDao.deleteByExamples(ids);
            specificationDao.deleteByPrimaryKeys(ids);
        }

    }

    @Override
    public List<Map<String, String>> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
