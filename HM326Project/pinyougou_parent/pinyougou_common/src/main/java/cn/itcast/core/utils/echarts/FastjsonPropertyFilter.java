package cn.itcast.core.utils.echarts;

import com.alibaba.fastjson.serializer.PropertyFilter;

import java.util.HashSet;
import java.util.Set;

public class FastjsonPropertyFilter implements PropertyFilter {  
    
    private final Set<String> includes = new HashSet<String>();  
    private final Set<String> excludes = new HashSet<String>();  
      
    public boolean apply(Object source, String name, Object value) {  
        if (excludes.contains(name)) {  
            return false;  
        }  
        if (excludes.contains(source.getClass().getSimpleName() + "." + name)) {  
            return false;  
        }  
        if (includes.size() == 0 || includes.contains(name)) {  
            return true;  
        }  
        if (includes.contains(source.getClass().getSimpleName() + "." + name)) {  
            return true;  
        }  
        return false;  
    }  
      
    public Set<String> getIncludes() {  
        return includes;  
    }  
      
    public Set<String> getExcludes() {  
        return excludes;  
    }  
      
}  