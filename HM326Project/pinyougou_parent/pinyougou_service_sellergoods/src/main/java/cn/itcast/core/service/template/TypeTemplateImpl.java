package cn.itcast.core.service.template;

import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateImpl implements TypeTemplateService {

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public PageInfo<TypeTemplate> search(Integer page, Integer rows, TypeTemplate typeTemplate) {

        List<TypeTemplate> list = typeTemplateDao.selectByExample(null);
        if (list!=null && list.size()>0){
            for (TypeTemplate template : list) {
                List<Map> brandList = JSON.parseArray(template.getBrandIds(), Map.class);
                redisTemplate.boundHashOps("brandList").put(template.getId(),brandList);
                List<Map> specList = findBySpecList(template.getId());
                redisTemplate.boundHashOps("specList").put(template.getId(),specList);
            }
        }

        PageHelper.startPage(page,rows);
        TypeTemplateQuery typeTemplateQuery=new TypeTemplateQuery();
        if (typeTemplate.getName()!=null && !"".equals(typeTemplate.getName().trim())){
            typeTemplateQuery.createCriteria().andNameLike("%"+typeTemplate.getName().trim()+"%");
        }

        PageHelper.orderBy("id desc");

        List<TypeTemplate> typeTemplateList = typeTemplateDao.selectByExample(typeTemplateQuery);
        PageInfo<TypeTemplate> pageInfo = new PageInfo<>(typeTemplateList);
        return pageInfo;
    }

    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplate.setStatus("0");
        typeTemplateDao.insertSelective(typeTemplate);
    }

    @Override
    public TypeTemplate findOne(long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    @Override
    public List<Map> findBySpecList(long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        // 例如：[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        List<Map> list = JSON.parseArray(specIds, Map.class);
        for (Map map : list) {
            long specId = Long.parseLong(map.get("id").toString());

            SpecificationOptionQuery specificationOptionQuery=new SpecificationOptionQuery();
            specificationOptionQuery.createCriteria().andSpecIdEqualTo(specId);
            List<SpecificationOption> specificationOptions = specificationOptionDao.selectByExample(specificationOptionQuery);
            map.put("options",specificationOptions);

        }
        return list;
    }

    //查询status==1的模板
    @Override
    public PageResult searchTemlListByStatus(Integer page, Integer rows, TypeTemplate typeTemplate) {
        PageHelper.startPage(page,rows);
        TypeTemplateQuery typeTemplateQuery=null;
        if(typeTemplate!=null){
            typeTemplateQuery=new TypeTemplateQuery();
            TypeTemplateQuery.Criteria criteria = typeTemplateQuery.createCriteria();
            if(typeTemplate.getName()!=null && !"".equals(typeTemplate.getName().trim())){
                criteria.andNameLike("%"+typeTemplate.getName()+"%");
            }
            if(typeTemplate.getStatus()!=null && !"".equals(typeTemplate.getStatus().trim())){
                criteria.andStatusEqualTo(typeTemplate.getStatus().trim());
            }

            typeTemplateQuery.setOrderByClause("id desc");
        }
        Page<TypeTemplate>page1= (Page<TypeTemplate>) typeTemplateDao.selectByExample(typeTemplateQuery);


        return new PageResult(page1.getTotal(),page1.getResult());
    }

    //审核方法
    @Override
    @Transactional
    public void updateStatus(long[] ids, String status) {
        if(ids!=null && ids.length>0){
            TypeTemplate typeTemplate=new TypeTemplate();
            typeTemplate.setStatus(status);
            for (long id : ids) {
                typeTemplate.setId(id);
                typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
            }
        }
    }

    /**商品分类添加 查询模板信息
     * @return {id:"",text:" "} 格式
     */
    @Override
    public List<Map> selectTypeTemplateList() {
        return typeTemplateDao.selectTypeTemplateList();
    }


    @Override
    public void delete(long[] ids) {
        if (ids!=null&&ids.length>0){
            for (long id : ids) {
                typeTemplateDao.deleteByPrimaryKey(id);
            }
        }

    }
}
