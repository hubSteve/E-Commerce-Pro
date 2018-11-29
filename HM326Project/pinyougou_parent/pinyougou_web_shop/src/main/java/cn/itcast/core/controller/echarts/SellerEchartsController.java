package cn.itcast.core.controller.echarts;

import cn.itcast.core.service.echarts.SellerEchartsService;
import cn.itcast.core.utils.echarts.JsonUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("sellerEcharts")
public class SellerEchartsController {

    @Reference
    private SellerEchartsService sellerEchartsService;

    @RequestMapping("zhexian")
    public void getLineDate(HttpServletRequest req, HttpServletResponse resp){
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        LinkedHashMap<String, Double> map = sellerEchartsService.echartsData(sellerId);
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
}
