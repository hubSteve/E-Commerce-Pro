package cn.itcast.core.controller.content;

import java.util.List;

import cn.itcast.core.entity.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.itcast.core.pojo.ad.Content;
import cn.itcast.core.service.content.ContentService;


@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;

	@RequestMapping("/findAll")
	public List<Content> list() throws Exception {
		List<Content> list = contentService.findAll();
		return list;
	}

	@RequestMapping("/findPage")
	public PageInfo<Content> findPage(Integer page, Integer rows) throws Exception {
		return contentService.findPage(null,page,rows);
	}

	@RequestMapping("/findOne")
	public Content findOne(Long id) throws Exception {
		Content content = contentService.findOne(id);
		return content;
	}

	@RequestMapping("/update")
	public Result update(@RequestBody Content content) throws Exception {
		try {
			contentService.edit(content);
			return new Result(true, "修改成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败!");
		}
	}

	@RequestMapping("/add")
	public Result add(@RequestBody Content content) throws Exception {
		try {
			contentService.add(content);
			return new Result(true, "保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "保存失败!");
		}
	}

	@RequestMapping("/delete")
	public Result delete(Long[] ids) throws Exception {
		try {
			contentService.delAll(ids);
			return new Result(true, "删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败!");
		}
	}

	@RequestMapping("/search")
	public PageInfo<Content> search(@RequestBody Content content, Integer page, Integer rows) throws Exception {
		PageInfo<Content> pageInfo = contentService.findPage(content, page, rows);
		return pageInfo;
	}
}
