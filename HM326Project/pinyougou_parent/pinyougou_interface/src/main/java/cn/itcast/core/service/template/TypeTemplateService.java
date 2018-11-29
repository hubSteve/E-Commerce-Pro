package cn.itcast.core.service.template;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {

    PageInfo<TypeTemplate> search(Integer page,Integer rows,TypeTemplate typeTemplate);

    void add(TypeTemplate typeTemplate);

    TypeTemplate findOne(long id);

    void update(TypeTemplate typeTemplate);

    List<Map> findBySpecList(long id);

    //查询所有status==1的模板
    PageResult searchTemlListByStatus(Integer page, Integer rows, TypeTemplate typeTemplate);

    //审核的方法
    void updateStatus(long[]ids,String status);


    /**商品分类添加 查询模板信息
     * @return {id:"",text:" "} 格式
     */
    List<Map> selectTypeTemplateList();


    void delete(long[] ids);
}
