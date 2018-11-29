package cn.itcast.core.utils.echarts;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class JsonUtils {
	public static void writeJson(Object object,   
            HttpServletRequest request, HttpServletResponse response) {  
        writeJsonByFilter(object, null, null, request, response);  
    }  
      
    public static void writeJsonByIncludesProperties(Object object, String[] includesProperties,  
            HttpServletRequest request, HttpServletResponse response) {  
        writeJsonByFilter(object, includesProperties, null, request, response);  
    }  
      
    public static void writeJsonByExcludesProperties(Object object, String[] excludesProperties,  
            HttpServletRequest request, HttpServletResponse response) {  
        writeJsonByFilter(object, null, excludesProperties, request, response);  
    }  
      
    public static void writeJsonByFilter(Object object, String[] includesProperties,  
            String[] excludesProperties, HttpServletRequest request, HttpServletResponse response) {  
        response.setContentType("text/html;charset=utf-8");  
        response.setHeader("Cache-Control", "no-cache");  
        PrintWriter writer = null;  
        try {  
            writer = response.getWriter();  
            FastjsonPropertyFilter filter = new FastjsonPropertyFilter();  
            if (includesProperties != null && includesProperties.length > 0) {  
                filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));  
            }  
            if (excludesProperties != null && excludesProperties.length > 0) {  
                filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));  
            }  
            String userAgent = request.getHeader("User-Agent");  
            if (userAgent.indexOf("MSIE") > -1 && (userAgent.indexOf("MSIE 6") > -1)) {  
                writer.write(JSON.toJSONString(object, filter,   
                        SerializerFeature.WriteDateUseDateFormat,   
                        SerializerFeature.DisableCircularReferenceDetect,   
                        SerializerFeature.BrowserCompatible));  
            } else {  
                writer.write(JSON.toJSONString(object, filter,   
                        SerializerFeature.WriteDateUseDateFormat,   
                        SerializerFeature.DisableCircularReferenceDetect));  
            }  
            writer.flush();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (writer != null) {  
                writer.close();  
            }  
        }  
    }  
}
