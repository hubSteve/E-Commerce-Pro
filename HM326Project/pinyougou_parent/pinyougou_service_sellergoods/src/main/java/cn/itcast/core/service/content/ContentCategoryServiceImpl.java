package cn.itcast.core.service.content;

import java.util.List;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;

import cn.itcast.core.dao.ad.ContentCategoryDao;
import cn.itcast.core.pojo.ad.ContentCategory;


@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private ContentCategoryDao contentCategoryDao;

	@Override
	public List<ContentCategory> findAll() {
		List<ContentCategory> list = contentCategoryDao.selectByExample(null);
		return list;
	}

	@Override
	public PageInfo<ContentCategory> findPage(ContentCategory contentCategory, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<ContentCategory> contentCategoryList = contentCategoryDao.selectByExample(null);
		PageInfo<ContentCategory> pageInfo=new PageInfo<>(contentCategoryList);
		return pageInfo;
	}

	@Override
	public void add(ContentCategory contentCategory) {
		contentCategoryDao.insertSelective(contentCategory);
	}

	@Override
	public void edit(ContentCategory contentCategory) {
		contentCategoryDao.updateByPrimaryKeySelective(contentCategory);
	}

	@Override
	public ContentCategory findOne(Long id) {
		ContentCategory category = contentCategoryDao.selectByPrimaryKey(id);
		return category;
	}

	@Override
	public void delAll(Long[] ids) {
		if(ids != null){
			for(Long id : ids){
				contentCategoryDao.deleteByPrimaryKey(id);
			}
		}
		
	}



}
