package cn.itcast.core.controller.echarts;

import cn.itcast.core.service.echarts.ManagerEchartsService;
import cn.itcast.core.utils.echarts.JsonUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("managerEcharts")
public class ManagerEchartsController {

    @Reference
    private ManagerEchartsService managerEchartsService;

    @RequestMapping("zhexian")
    public void getLineData(HttpServletRequest req, HttpServletResponse resp){
        LinkedHashMap<String, Double> map = managerEchartsService.echartsData();
        if(map==null) return;
        Set<String> set = map.keySet();
        Collection<Double> values1 = map.values();
        String[] data2 = set.toArray(new String[set.size()]);
        Double[] datas = values1.toArray(new Double[values1.size()]);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("data2", data2);
        json.put("datas", datas);
        JsonUtils.writeJson(json, req, resp);
    }

    @RequestMapping("pie")
    public void getPieData(HttpServletRequest req, HttpServletResponse resp){
        LinkedHashMap<String, Double> map = managerEchartsService.echartsData();
        if(map==null) return;
        Set<String> set = map.keySet();
        Collection<Double> values1 = map.values();
        String[] label = set.toArray(new String[set.size()]);
        Double[] value = values1.toArray(new Double[values1.size()]);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("label", label);
        json.put("value", value);
        JsonUtils.writeJson(json, req, resp);
    }

}
