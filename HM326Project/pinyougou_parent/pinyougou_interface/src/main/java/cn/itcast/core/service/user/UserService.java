package cn.itcast.core.service.user;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface UserService {

    void add(User user);

    void createSmsCode(String phone);

    boolean checkSmsCode(String phone,String code);

    List<Order> findCartList(String userId);

    PageInfo<Order> abfindCartList(Integer Page, Integer pageSize, String username );
}
