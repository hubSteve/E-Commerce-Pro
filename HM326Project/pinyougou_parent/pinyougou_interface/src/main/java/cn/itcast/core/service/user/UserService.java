package cn.itcast.core.service.user;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.aspectj.weaver.ast.Or;


import java.util.List;

public interface UserService {

    void add(User user);

    void createSmsCode(String phone);

    boolean checkSmsCode(String phone,String code);

    //运营商后台查询用户
    PageInfo<User> search(int pageNum, int pageSize, User user);

    /** 运营商后台修改用户状态 回显数据
     * @param id
     * @return
     */
    User findOne(long id);


    /** 运营商后天更新用户状态
     * @param user
     */
    void update(User user);

    /**运营商删除用户
     * @param ids
     */
    void delete(long[] ids);

    List<Order> findCartList(String userId);


    PageInfo<Order> abfindCartList(Integer Page, Integer pageSize, String username );


}
