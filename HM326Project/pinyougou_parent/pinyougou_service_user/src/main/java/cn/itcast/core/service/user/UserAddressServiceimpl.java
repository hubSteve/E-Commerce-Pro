package cn.itcast.core.service.user;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

//个人中心
@Service
@Transactional
public class UserAddressServiceimpl implements UserAddressService {


    @Resource
    private UserDao userDao;


    //添加个人信息
    @Override
    public void update(User user) {
        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andUsernameEqualTo(user.getUsername());
        List<User> users = userDao.selectByExample(userQuery);
        for (User user1 : users) {
            user1.setNickName(user.getNickName());
            user1.setSex(user.getSex());
            userDao.updateByPrimaryKey(user1);
        }

    }
}
