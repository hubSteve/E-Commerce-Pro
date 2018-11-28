package cn.itcast.core.service.content;

import java.util.List;

import cn.itcast.core.pojo.ad.ContentQuery;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;

import cn.itcast.core.dao.ad.ContentDao;
import cn.itcast.core.pojo.ad.Content;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentDao contentDao;

	@Resource
	private RedisTemplate redisTemplate;

	@Override
	public List<Content> findAll() {
		List<Content> list = contentDao.selectByExample(null);
		return list;
	}

	@Override
	public PageInfo<Content> findPage(Content content, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Content> contentList = contentDao.selectByExample(null);
		PageInfo<Content> pageInfo=new PageInfo<>(contentList);
		return pageInfo;
	}

	@Transactional
	@Override
	public void add(Content content) {
		clearCache(content.getCategoryId());
		contentDao.insertSelective(content);
	}

	private void clearCache(Long categoryId) {
		redisTemplate.boundHashOps("content").delete(categoryId);
	}

	@Transactional
	@Override
	public void edit(Content content) {
		Long newCategoryId = content.getCategoryId();
		Long oldCategoryId = contentDao.selectByPrimaryKey(content.getId()).getCategoryId();
		if (newCategoryId != oldCategoryId){
			clearCache(newCategoryId);
			clearCache(oldCategoryId);
		}else {
			clearCache(oldCategoryId);
		}
		contentDao.updateByPrimaryKeySelective(content);
	}

	@Override
	public Content findOne(Long id) {
		Content content = contentDao.selectByPrimaryKey(id);
		return content;
	}

	@Transactional
	@Override
	public void delAll(Long[] ids) {
		if(ids != null){
			for(Long id : ids){
				Content content = contentDao.selectByPrimaryKey(id);
				clearCache(content.getCategoryId());
				contentDao.deleteByPrimaryKey(id);
			}
		}
	}

	@Override
	public List<Content> findByCategoryId(long categoryId) {
		List<Content> contentList = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
		if (contentList==null){
			synchronized (this){
				contentList = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
				if (contentList==null){
					ContentQuery contentQuery=new ContentQuery();
					contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");
					contentList = contentDao.selectByExample(contentQuery);
					redisTemplate.boundHashOps("content").put(categoryId,contentList);
				}
			}
		}

		return contentList;
	}


}
