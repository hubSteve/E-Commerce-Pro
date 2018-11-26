package cn.itcast.core.service.content;

import java.util.List;

import cn.itcast.core.pojo.ad.Content;
import com.github.pagehelper.PageInfo;


public interface ContentService {

	public List<Content> findAll();

	public PageInfo<Content> findPage(Content content, Integer pageNum, Integer pageSize);

	public void add(Content content);

	public void edit(Content content);

	public Content findOne(Long id);

	public void delAll(Long[] ids);

	List<Content> findByCategoryId(long categoryId);

}
