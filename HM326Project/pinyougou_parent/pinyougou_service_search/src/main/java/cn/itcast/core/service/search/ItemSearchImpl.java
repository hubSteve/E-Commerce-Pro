package cn.itcast.core.service.search;

import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ItemSearchImpl implements ItemSearchService {

    @Resource
    private SolrTemplate solrTemplate;
    
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();

        String keywords = searchMap.get("keywords");
        if (keywords!=null && !"".equals(keywords)){
            keywords=keywords.replace(" ","");
            searchMap.put("keywords",keywords);
        }

        //Map<String, Object> map = searchForPage(searchMap);
        Map<String, Object> map = searchForHighlightPage(searchMap);
        resultMap.putAll(map);

        List<String> categoryList =searchForGroupPage(searchMap);
        if (categoryList!=null && categoryList.size()>0){
            resultMap.put("categoryList",categoryList);
            Map<String, Object> brandAndSpecMap = searchBrandAndSpecListByCategory(categoryList.get(0));
            resultMap.putAll(brandAndSpecMap);
        }
        return resultMap;
    }

    private Map<String,Object> searchBrandAndSpecListByCategory(String category) {
        Map<String,Object> brandAndSpecMap=new HashMap<>();
        Object typeId = redisTemplate.boundHashOps("itemCat").get(category);
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
        brandAndSpecMap.put("brandList",brandList);
        brandAndSpecMap.put("specList",specList);
        return brandAndSpecMap;
    }

    private List<String> searchForGroupPage(Map<String,String> searchMap) {
        List<String> list=new ArrayList<>();
        String keywords = searchMap.get("keywords");
        Criteria criteria=new Criteria("item_keywords");
        if (keywords!=null && !"".equals(keywords)){
            criteria.is(keywords);
        }
        Query query = new SimpleQuery(criteria);
        GroupOptions groupOptions=new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue();
            list.add(groupValue);
        }
        return list;
    }

    private Map<String,Object> searchForHighlightPage(Map<String,String> searchMap) {
        Map<String, Object> map = new HashMap<>();
        String keywords = searchMap.get("keywords");
        Criteria criteria=new Criteria("item_keywords");
        if (keywords!=null && !"".equals(keywords)){
            criteria.is(keywords);
        }
        HighlightQuery query=new SimpleHighlightQuery(criteria);
        //设置起始页和每页条数
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize=Integer.valueOf(searchMap.get("pageSize"));
        Integer startRow=(pageNo-1)*pageSize;
        query.setOffset(startRow);
        query.setRows(pageSize);

        //设置高亮
        HighlightOptions highlightOptions =new HighlightOptions();
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);

        //添加过滤条件
        //分类
        String category = searchMap.get("category");
        if (category!=null && !"".equals(category)){
            Criteria cri=new Criteria("item_category");
            cri.is(category);
            FilterQuery filterQuery=new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }

        //品牌
        String brand = searchMap.get("brand");
        if (brand!=null && !"".equals(brand)){
            Criteria cri=new Criteria("item_brand");
            cri.is(brand);
            FilterQuery filterQuery=new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }

        //规格
        String spec = searchMap.get("spec");
        if (spec!=null && !"".equals(spec)){
            Map<String,String> map1 = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entrySet = map1.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                Criteria cri=new Criteria("item_spec_"+entry.getKey());
                cri.is(entry.getValue());
                SimpleFilterQuery filterQuery=new SimpleFilterQuery(cri);
                query.addFilterQuery(filterQuery);
            }
        }

        //价格
        String price = searchMap.get("price");
        if (price!=null && !"".equals(price)){
            Criteria cri=new Criteria("item_price");
            String[] prices = price.split("-");
            cri.between(prices[0],prices[1],true,true);
            FilterQuery filterQuery=new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }

        // 结果排序：新品、价格
        // 根据新品排序：sortField，排序字段   sort：排序规则
        String s = searchMap.get("sort");
        if (s!=null && !"".equals(s)){
            if ("ASC".equals(s)){
                Sort sort=new Sort(Sort.Direction.ASC,"item_"+searchMap.get("sortField"));
                query.addSort(sort);
            }else {
                Sort sort=new Sort(Sort.Direction.DESC,"item_"+searchMap.get("sortField"));
                query.addSort(sort);
            }

        }

        HighlightPage<Item> highlightPage = solrTemplate.queryForHighlightPage(query, Item.class);
        //处理结果集
        List<HighlightEntry<Item>> highlighted = highlightPage.getHighlighted();
        if (highlighted!=null && highlighted.size()>0){
            for (HighlightEntry<Item> highlightEntry : highlighted) {
                Item item = highlightEntry.getEntity();
                List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
                if (highlights!=null && highlights.size()>0){
                    for (HighlightEntry.Highlight highlight : highlights) {
                        String str = highlight.getSnipplets().get(0);
                        item.setTitle(str);
                    }
                }
            }
        }

        map.put("totalPages", highlightPage.getTotalPages());
        map.put("total",highlightPage.getTotalElements());
        map.put("rows",highlightPage.getContent());
        return map;
    }

    private Map<String,Object> searchForPage(Map<String,String> searchMap) {
        Map<String, Object> map = new HashMap<>();
        String keywords = searchMap.get("keywords");
        Criteria criteria=new Criteria("item_keywords");
        if (keywords!=null && !"".equals(keywords)){
            criteria.is(keywords);
        }
        Query query = new SimpleQuery(criteria);
        //设置起始页和每页条数
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize=Integer.valueOf(searchMap.get("pageSize"));
        Integer startRow=(pageNo-1)*pageSize;
        query.setOffset(startRow);
        query.setRows(pageSize);

        ScoredPage<Item> scoredPage = solrTemplate.queryForPage(query, Item.class);
        map.put("totalPages", scoredPage.getTotalPages());
        map.put("total",scoredPage.getTotalElements());
        map.put("rows",scoredPage.getContent());
        return map;
    }
}
