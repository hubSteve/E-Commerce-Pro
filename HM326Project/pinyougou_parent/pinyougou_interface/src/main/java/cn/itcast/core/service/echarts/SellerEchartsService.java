package cn.itcast.core.service.echarts;

import java.util.LinkedHashMap;

public interface SellerEchartsService {

    LinkedHashMap<String,Double> echartsData(String sellerId);
}
